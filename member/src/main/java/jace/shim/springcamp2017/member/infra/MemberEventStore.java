package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.*;
import jace.shim.springcamp2017.member.model.event.MemberRawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
@Slf4j
public class MemberEventStore implements EventStore<String> {

	@Autowired
	private MemberEventStoreRepository memberEventStoreRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private EventProjector eventProjector;

	@Override
	public void saveEvents(final String identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<MemberRawEvent> rawEvents = memberEventStoreRepository.findByIdentifier(identifier);
			Long actualVersion = rawEvents.stream()
				.sorted((e1, e2) -> Long.compare(e2.getVersion(), e1.getVersion()))
				.findFirst().map(MemberRawEvent::getVersion)
				.orElse(-1L);

			if (! expectedVersion.equals(actualVersion)) {
				String exceptionMessage = String.format("Unmatched Version : expected: {}, actual: {}", expectedVersion, actualVersion);
				throw new IllegalStateException(exceptionMessage);
			}
		}

		for (Event event : events) {
			String type = event.getClass().getName();
			String payload = null;

			try {
				payload = objectMapper.writeValueAsString(event);
				log.debug("-> payload : {}", payload);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}

			expectedVersion = increaseVersion(expectedVersion);
			LocalDateTime now = LocalDateTime.now();
			MemberRawEvent rawEvent = new MemberRawEvent(identifier, type, expectedVersion, payload, now);

			// 이벤트 저장
			memberEventStoreRepository.save(rawEvent);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	private Long increaseVersion(Long expectedVersion) {
		return ++expectedVersion;
	}

	@Override
	public List<Event<String>> getEvents(String identifier) {
		final List<MemberRawEvent> rawEvents = memberEventStoreRepository.findByIdentifier(identifier);
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<String>> getAllEvents() {
		final List<MemberRawEvent> rawEvents = memberEventStoreRepository.findAll();
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<String>> getEventsByAfterVersion(String identifier, Long version) {
		final List<MemberRawEvent> rawEvents = memberEventStoreRepository.findByIdentifierAndVersionGreaterThan(identifier, version);
		return convertEvent(rawEvents);
	}

	private List<Event<String>> convertEvent(List<MemberRawEvent> rawEvents) {
		return rawEvents.stream().map(rawEvent -> {
			Event<String> event = null;
			try {
				log.debug("-> event info : {}", rawEvent.toString());
				event = (Event) objectMapper.readValue(rawEvent.getPayload(), Class.forName(rawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", rawEvent.getIdentifier(), rawEvent.getType(),
					rawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}

}
