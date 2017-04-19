package jace.shim.springcamp2017.product.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventProjector;
import jace.shim.springcamp2017.core.event.EventPublisher;
import jace.shim.springcamp2017.core.event.EventStore;
import jace.shim.springcamp2017.product.model.event.ProductRawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class ProductEventStore implements EventStore<Long> {

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private EventProjector eventProjector;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@Transactional
	public void saveEvents(final Long identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<ProductRawEvent> productRawEvents = productEventStoreRepository.findByIdentifier(identifier);
			Long actualVersion = productRawEvents.stream()
				.sorted(Comparator.comparing(ProductRawEvent::getVersion))
				.findFirst().map(ProductRawEvent::getVersion)
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
			ProductRawEvent rawEvent = new ProductRawEvent(identifier, type, expectedVersion, payload, now);

			productEventStoreRepository.save(rawEvent);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	@Override
	public List<Event<Long>> getEvents(Long identifier) {
		final List<ProductRawEvent> productRawEvents = productEventStoreRepository.findByIdentifier(identifier);
		return convertEvent(productRawEvents);
	}

	@Override
	public List<Event<Long>> getAllEvents() {
		final List<ProductRawEvent> productRawEvents = productEventStoreRepository.findAll();
		return convertEvent(productRawEvents);
	}

	@Override
	public List<Event<Long>> getEventsByAfterVersion(Long identifier, Long version) {
		final List<ProductRawEvent> productRawEvents = productEventStoreRepository.findByIdentifierAndVersionGreaterThan(identifier, version);
		return convertEvent(productRawEvents);
	}

	private List<Event<Long>> convertEvent(List<ProductRawEvent> productRawEvents) {
		return productRawEvents.stream().map(productRawEvent -> {
			Event<Long> event = null;
			try {
				event = (Event) objectMapper.readValue(productRawEvent.getPayload(), Class.forName(productRawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", productRawEvent.getSeq(), productRawEvent.getType(),
					productRawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}
}
