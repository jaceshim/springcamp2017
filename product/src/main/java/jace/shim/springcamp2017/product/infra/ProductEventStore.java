package jace.shim.springcamp2017.product.infra;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
@Slf4j
public class ProductEventStore implements EventStore<Long> {

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EventPublisher eventPublisher;

	@Override
	public void saveEvents(final Long identifier, Long expectedVersion, final List<Event> events) {

		List<RawEvent> rawEvents = Collections.EMPTY_LIST;
		// 신규 등록된 aggregate가 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			rawEvents = productEventStoreRepository.findByIdentifer(identifier);
			Long actualVersion = rawEvents.stream()
				.sorted(Comparator.comparing(RawEvent::getVersion))
				.findFirst().map(RawEvent::getVersion)
				.orElse(-1L);

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
			RawEvent rawEvent = new RawEvent(identifier, type, expectedVersion, payload, now);

			productEventStoreRepository.save(rawEvent);

			// event 발행
			eventPublisher.publish(event);
		}
	}

	@Override
	public List<Event> getEvents(Long identifier) {
		final List<RawEvent> rawEvents = productEventStoreRepository.findByIdentifer(identifier);
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event> getEventsByAfterVersion(Long identifier, Long version) {
		final List<RawEvent> rawEvents = productEventStoreRepository.findByIdentiferAndVersionGreaterThan(identifier, version);
		return convertEvent(rawEvents);
	}

	private List<Event> convertEvent(List<RawEvent> rawEvents) {
		return rawEvents.stream().map(rawEvent -> {
			Event event = null;
			try {
				event = (Event) objectMapper.readValue(rawEvent.getPayload(), Class.forName(rawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", rawEvent.getId(), rawEvent.getType(),
					rawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}

}
