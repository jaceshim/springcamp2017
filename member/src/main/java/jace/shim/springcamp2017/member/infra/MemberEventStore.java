package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventPublisher;
import jace.shim.springcamp2017.core.event.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
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

	@Override
	public void saveEvents(final String identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록된 aggregate가 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<MemberRawEvent> memberRawEvents = memberEventStoreRepository.findByIdentifer(identifier);
			Long actualVersion = memberRawEvents.stream()
				.sorted(Comparator.comparing(MemberRawEvent::getVersion))
				.findFirst().map(MemberRawEvent::getVersion)
				.orElse(null);

			if (expectedVersion != actualVersion) {
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

			expectedVersion++;
			LocalDateTime now = LocalDateTime.now();
			MemberRawEvent memberRawEvent = new MemberRawEvent(identifier, type, expectedVersion, payload, now);

			memberEventStoreRepository.save(memberRawEvent);

			// event 발행
			eventPublisher.publish(event);
		}
	}

	@Override
	public List<Event<String>> getEvents(String identifier) {
		final List<MemberRawEvent> memberRawEvents = memberEventStoreRepository.findByIdentifer(identifier);
		return convertEvent(memberRawEvents);
	}

	@Override
	public List<Event<String>> getAllEvents() {
		final List<MemberRawEvent> memberRawEvents = memberEventStoreRepository.findAll();
		return convertEvent(memberRawEvents);
	}

	@Override
	public List<Event<String>> getEventsByAfterVersion(String identifier, Long version) {
		final List<MemberRawEvent> memberRawEvents = memberEventStoreRepository.findByIdentiferAndVersionGreaterThan(identifier, version);
		return convertEvent(memberRawEvents);
	}

	private List<Event<String>> convertEvent(List<MemberRawEvent> memberRawEvents) {
		return memberRawEvents.stream().map(memberRawEvent -> {
			Event<String> event = null;
			try {
				event = (Event) objectMapper.readValue(memberRawEvent.getPayload(), Class.forName(memberRawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", memberRawEvent.getSeq(), memberRawEvent.getType(),
					memberRawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}

}
