package jace.shim.springcamp2017.order.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Getter
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
