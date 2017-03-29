package jace.shim.springcamp2017.order.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
public class Order extends AggregateRoot<Long> {
	/** 주문번호 */
	private Long orderId;
	/** 주문자 아이디 */
	private String memberId;
	/** 주문 상품 */
	private List<OrderLine> orderLine;

	public Order(Long orderId) {
		super(orderId);
	}
}
