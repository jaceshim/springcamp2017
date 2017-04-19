package jace.shim.springcamp2017.order.model.event;

import jace.shim.springcamp2017.member.model.Member;
import jace.shim.springcamp2017.order.model.Delivery;
import jace.shim.springcamp2017.order.model.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Getter
@NoArgsConstructor
public class OrderCreated extends AbstractOrderEvent {
	private Member orderMember;
	private Delivery delivery;
	private Set<OrderItem> orderItems;
	private LocalDateTime created;

	public OrderCreated(Long orderId, Member orderMember, Delivery delivery, Set<OrderItem> orderItems, LocalDateTime created) {
		this.orderId = orderId;
		this.orderMember = orderMember;
		this.delivery = delivery;
		this.orderItems = orderItems;
		this.created = created;
	}

	public Long getOrderId() {
		return this.orderId;
	}
}
