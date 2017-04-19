package jace.shim.springcamp2017.product.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.order.model.OrderItem;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
import jace.shim.springcamp2017.product.model.command.ProductCommand;
import jace.shim.springcamp2017.product.model.event.ProductRawEvent;
import jace.shim.springcamp2017.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Component
@Slf4j
public class ProductEventListener extends AbstractEventListener {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@org.springframework.kafka.annotation.KafkaListener(id = "order-consumer-group", topics = "order-event-topic")
	public void orderEventListener(String message) throws IOException, ClassNotFoundException {
		log.debug("receive message from order : {}", message);

		ProductRawEvent rawEvent = objectMapper.readValue(message, ProductRawEvent.class);
		final Class<?> eventType = Class.forName(rawEvent.getType());
		final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

		this.handle(event);
	}

	/**
	 * 주문 이벤트 처리 - 상품재고 차감
	 *
	 * @param event
	 */
	public void execute(OrderCreated event) {
		// todo 상품재고 수량 차감 이벤트를 발생 시켜야 하는데.
		final Set<OrderItem> orderItems = event.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			ProductCommand.DecreaseQuantity productDecreaseQuantityCommand = new ProductCommand.DecreaseQuantity(orderItem.getQuantity());
			productService.decreaseQuantity(orderItem.getProduct().getProductId(), productDecreaseQuantityCommand);
		}
	}
}
