package jace.shim.springcamp2017.order.model.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jace.shim.springcamp2017.member.model.read.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 31..
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

	/** 주문번호 */
	private Long orderId;

	/** 주문회원 */
	private Member orderMember;

	/** 배송정보 */
	private Delivery delivery;

	/** 주문 상품 */
	private List<OrderItem> orderItems;

	private LocalDateTime created;

	public Order(Long orderId) {
		this.orderId = orderId;
	}

	public int getTotalOrderPrice() {
		return orderItems.stream().mapToInt(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity()).sum();
	}

	private static DecimalFormat PRICE_DECIMAL_FORMAT = new DecimalFormat("#,##0");
	public String getFormatTotalOrderPrice() {
		return PRICE_DECIMAL_FORMAT.format(this.getTotalOrderPrice());
	}
}
