package jace.shim.springcamp2017.order.infra;

import jace.shim.springcamp2017.core.event.AbstractEventProjector;
import jace.shim.springcamp2017.order.model.OrderItem;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
import jace.shim.springcamp2017.product.model.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Component
@Slf4j
public class OrderEventProjector extends AbstractEventProjector {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 상품주문 projection
	 * @param event
	 */
	public void execute(OrderCreated event) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO `order` (");
		query.append(" orderId, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			convertLocalDateTimeToTimestamp(event.getCreated()));

		query.setLength(0);

		query.append("INSERT INTO order_member (");
		query.append(" orderId, memberId ");
		query.append(") VALUES ( ");
		query.append(" ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			event.getOrderMember().getId());

		query.setLength(0);

		query.append("INSERT INTO order_delivery (");
		query.append(" orderId, address, phone, deliveryMessage ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			event.getDelivery().getAddress(),
			event.getDelivery().getPhone(),
			event.getDelivery().getDeliveryMessage());

		query.setLength(0);

		query.append("INSERT INTO order_items (");
		query.append(" orderId, productId, price, quantity ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?) ");

		final Set<OrderItem> orderItems = event.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			jdbcTemplate.update(query.toString(),
				event.getOrderId(),
				orderItem.getProduct().getProductId(),
				orderItem.getProduct().getPrice(),
				orderItem.getQuantity());
		}
	}

	/**
	 * 상품 등록 projection
	 * @param event
	 */
	public void execute(ProductCreated event) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO product (");
		query.append(" productId, name, price, inventory, imagePath, description, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?, ?, ?, ? ) ");

		jdbcTemplate.update(query.toString(),
			event.getProductId(),
			event.getName(),
			event.getPrice(),
			event.getInventory(),
			event.getImagePath(),
			event.getDescription(),
			convertLocalDateTimeToTimestamp(event.getCreated()));
	}

	/**
	 * 상품 명 변경 projection
	 *
	 * @param event
	 */
	public void execute(ProductNameChanged event) {
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
	public void execute(ProductPriceChanged event) {
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
	public void execute(ProductInventoryDecreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET inventory = (inventory - ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getInventory(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 수량 증가 projection
	 *
	 * @param event
	 */
	public void execute(ProductInventoryIncreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET inventory = (inventory + ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getInventory(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

}
