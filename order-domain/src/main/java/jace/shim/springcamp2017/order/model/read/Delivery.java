package jace.shim.springcamp2017.order.model.read;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by jaceshim on 2017. 4. 6..
 */
@Getter
@Setter
@NoArgsConstructor
public class Delivery {
	private String address;

	private String phone;

	private String deliveryMessage;

	public Delivery(String address, String phone, String deliveryMessage) {
		this.address = address;
		this.phone = phone;
		this.deliveryMessage = deliveryMessage;
	}
}
