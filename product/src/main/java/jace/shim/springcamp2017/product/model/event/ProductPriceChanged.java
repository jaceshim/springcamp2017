package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductPriceChanged implements Event {
	private Long productId;

	private int price;

	private LocalDateTime updated;

	public ProductPriceChanged() {
	}

	public ProductPriceChanged(Long identifier, int price) {
		this.productId = identifier;
		this.price = price;
		this.updated = LocalDateTime.now();
	}
}
