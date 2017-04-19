package jace.shim.springcamp2017.order.model.read;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jaceshim on 2017. 3. 31..
 */
@Getter
@NoArgsConstructor
public class Order {

	private Long orderId;

	public Order(Long orderId) {
		this.orderId = orderId;
	}
}
