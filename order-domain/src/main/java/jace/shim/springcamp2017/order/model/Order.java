package jace.shim.springcamp2017.order.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.member.model.Member;
import jace.shim.springcamp2017.order.exception.OrderInventoryExceedLimit;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
import jace.shim.springcamp2017.product.model.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Getter
@EqualsAndHashCode
public class Order extends AggregateRoot<Long> {
	/** 주문번호 */
	private Long orderId;

	/** 주문회원 */
	private Member orderMember;

	/** 배송정보 */
	private Delivery delivery;

	/** 주문 상품 */
	private Set<OrderItem> orderItems = new HashSet<>();

	private LocalDateTime created;

	Order() {
		super();
	}

	Order(Long orderId, Member orderMember, Delivery delivery, List<OrderItem> checkoutItems) {
		super(orderId);
		this.orderId = orderId;
		this.orderMember = orderMember;
		this.delivery = delivery;
		this.created = LocalDateTime.now();

		for (OrderItem orderItem : checkoutItems) {
			this.with(orderItem);
		}
		applyChange(new OrderCreated(orderId, orderMember, delivery, orderItems, created));
	}

	/**
	 * 주문 이벤트 반영
	 * @param orderCreated
	 */
	public void apply(OrderCreated orderCreated) {
		this.orderId = orderCreated.getOrderId();
		this.orderMember = orderCreated.getOrderMember();
		this.delivery = orderCreated.getDelivery();
		this.orderItems = orderCreated.getOrderItems();
	}

	public static Order order(Long orderId, Member orderMember, Delivery delivery, List<OrderItem> orderItems) {
		final Order order = new Order(orderId, orderMember, delivery, orderItems);
		return order;
	}

	public Order with(Product product, int quantity) {
		return with(new OrderItem(product, quantity));
	}

	private Order with(OrderItem orderItem) {
		if (inventoryExceedLimit(orderItem)) {
			throw new OrderInventoryExceedLimit(orderItem.getProduct().getProductId(), orderItem.getQuantity());
		}

		for (OrderItem item : orderItems) {
			if (item.isProductEqual(orderItem)) {
				item.merge(orderItem);
				return this;
			}
		}

		this.orderItems.add(orderItem);

		return this;
	}

	/**
	 * 재고수량 확인
	 *
	 * @param orderItem
	 * @return
	 */
	private boolean inventoryExceedLimit(OrderItem orderItem) {
		final int currentInventory = orderItem.getProduct().getInventory();
		return currentInventory < orderItem.getQuantity();
	}

	public int getTotalPrice() {
		return orderItems.stream().mapToInt(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity()).sum();
	}

	public Order with(String address, String phone, String deliveryMessage) {
		this.delivery = new Delivery(address, phone, deliveryMessage);
		return this;
	}
}
