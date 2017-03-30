package jace.shim.springcamp2017.order.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.product.infra.ProductRawEvent;
import jace.shim.springcamp2017.product.model.event.*;
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
			ProductRawEvent rawEvent = objectMapper.readValue(message, ProductRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			this.handle(event);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 상품 등록 projection
	 * @param event
	 */
	public void apply(ProductCreated event) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO product (");
		query.append(" productId, name, price, quantity, description, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?, ?, ? ) ");

		jdbcTemplate.update(query.toString(),
			event.getProductId(),
			event.getName(),
			event.getPrice(),
			event.getQuantity(),
			event.getDescription(),
			convertLocalDateTimeToTimestamp(event.getCreated()));
	}

	/**
	 * 상품 명 변경 projection
	 *
	 * @param event
	 */
	public void apply(ProductNameChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET name = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getName(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 가격 변경 projection
	 *
	 * @param event
	 */
	public void apply(ProductPriceChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET price = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getPrice(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 수량 감소 projection
	 *
	 * @param event
	 */
	public void apply(ProductQuantityDecreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET quantity = (quantity - ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getQuantity(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 수량 증가 projection
	 *
	 * @param event
	 */
	public void apply(ProductQuantityIncreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET quantity = (quantity + ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getQuantity(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}
}
