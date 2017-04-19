package jace.shim.springcamp2017.order.model.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jaceshim on 2017. 4. 6..
 */
public class OrderCommand {

	/**
	 * 주문
	 */
	@Getter
	@NoArgsConstructor
	public static class CreateOrder {

		private String memberId;

		@NotNull
		private List<OrderItem> orderItems;

		@NotNull
		private Delivery delivery;

		public CreateOrder(String memberId, List<OrderItem> orderItems, Delivery delivery) {
			this.memberId = memberId;
			this.orderItems = orderItems;
			this.delivery = delivery;
		}
	}

	/**
	 * 주문상품
	 */
	@Getter
	@NoArgsConstructor
	public static class OrderItem {

		private Long productId;

		private int quantity;

		public OrderItem(Long productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}

	/**
	 * 배송정보
	 */
	@Getter
	@NoArgsConstructor
	public static class Delivery {
		private String address;

		private String phone;

		private String deliveryMessage;

		public Delivery(String address, String phone, String deliveryMessage) {
			this.address = address;
			this.phone = phone;
			this.deliveryMessage = deliveryMessage;
		}
	}
}
