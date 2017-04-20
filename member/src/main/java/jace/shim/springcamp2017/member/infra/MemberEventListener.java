package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventProjector;
import jace.shim.springcamp2017.order.model.event.OrderRawEvent;
import jace.shim.springcamp2017.product.model.event.ProductRawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jaceshim on 2017. 3. 27..
 */
@Component
@Slf4j
public class MemberEventListener extends AbstractEventListener {

	@Autowired
	private ObjectMapper objectMapper;

	// projector는 event가 발생하는 domain단위로 분리하여 구현할 수도 있다
	@Autowired
	EventProjector eventProjector;

	@org.springframework.kafka.annotation.KafkaListener(id = "product-consumer-group", topics = "product-event-topic")
	public void productEventListener(String message) {
		log.debug("receive message from product : {}", message);

		try {
			ProductRawEvent rawEvent = objectMapper.readValue(message, ProductRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			this.handle(event);

			eventProjector.handle(event);

		} catch (IOException | ClassNotFoundException e) {
			log.warn(e.getMessage(), e);
		}
	}

	@org.springframework.kafka.annotation.KafkaListener(id = "order-consumer-group", topics = "order-event-topic")
	public void orderEventListener(String message) {
		log.debug("receive message from order : {}", message);

		try {
			OrderRawEvent rawEvent = objectMapper.readValue(message, OrderRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			this.handle(event);

			eventProjector.handle(event);

		} catch (IOException | ClassNotFoundException e) {
			log.warn(e.getMessage(), e);
		}
	}
}
