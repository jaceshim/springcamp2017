package jace.shim.springcamp2017.order.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventProjector;
import jace.shim.springcamp2017.core.event.EventPublisher;
import jace.shim.springcamp2017.core.event.EventStore;
import jace.shim.springcamp2017.order.model.event.OrderRawEvent;
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
public class OrderEventStore implements EventStore<Long> {

	@Autowired
	private OrderEventStoreRepository orderEventStoreRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private EventProjector eventProjector;

	@Override
	public void saveEvents(final Long identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<OrderRawEvent> rawEvents = orderEventStoreRepository.findByIdentifier(identifier);
			Long actualVersion = rawEvents.stream()
				.sorted(Comparator.comparing(OrderRawEvent::getVersion))
				.findFirst().map(OrderRawEvent::getVersion)
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
				log.debug("-> event payload : {}", payload);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}

			expectedVersion++;
			LocalDateTime now = LocalDateTime.now();
			OrderRawEvent rawEvent = new OrderRawEvent(identifier, type, expectedVersion, payload, now);

			orderEventStoreRepository.save(rawEvent);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	@Override
	public List<Event<Long>> getEvents(Long identifier) {
		final List<OrderRawEvent> rawEvents = orderEventStoreRepository.findByIdentifier(identifier);
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getAllEvents() {
		final List<OrderRawEvent> rawEvents = orderEventStoreRepository.findAll();
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getEventsByAfterVersion(Long identifier, Long version) {
		final List<OrderRawEvent> rawEvents = orderEventStoreRepository.findByIdentifierAndVersionGreaterThan(identifier, version);
		return convertEvent(rawEvents);
	}

	private List<Event<Long>> convertEvent(List<OrderRawEvent> rawEvents) {
		return rawEvents.stream().map(rawEvent -> {
			Event<Long> event = null;
			try {
				event = (Event) objectMapper.readValue(rawEvent.getPayload(), Class.forName(rawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", rawEvent.getSeq(), rawEvent.getType(),
					rawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}

}
