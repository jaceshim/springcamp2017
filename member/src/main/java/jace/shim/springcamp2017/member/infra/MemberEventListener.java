package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventProjector;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
import jace.shim.springcamp2017.order.model.event.OrderRawEvent;
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

	@Autowired
	EventProjector eventProjector;

	@org.springframework.kafka.annotation.KafkaListener(id = "order-consumer-group", topics = "order-event-topic")
	public void orderEventListener(String message) throws IOException, ClassNotFoundException {
		log.debug("receive message from order : {}", message);

		OrderRawEvent rawEvent = objectMapper.readValue(message, OrderRawEvent.class);
		final Class<?> eventType = Class.forName(rawEvent.getType());
		final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

		this.handle(event);
	}

	/**
	 * 상품주문 event 처리
	 * @param event
	 */
	public void execute(OrderCreated event) {
		eventProjector.handle(event);
	}
}
