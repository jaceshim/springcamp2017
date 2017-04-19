package jace.shim.springcamp2017.order.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.order.model.event.OrderRawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Component
@Slf4j
public class OrderEventListener extends AbstractEventListener {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@org.springframework.kafka.annotation.KafkaListener(id = "product-consumer-group", topics = "product-event-topic")
	public void productEventListener(String message) {
		log.debug("receive message : {}", message);

		try {
			OrderRawEvent rawEvent = objectMapper.readValue(message, OrderRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			this.handle(event);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
}
