package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductQuantityDecreased implements Event {
	private Long productId;

	private int quantity;

	private LocalDateTime updated;

	public ProductQuantityDecreased() {
	}

	public ProductQuantityDecreased(Long identifier, int quantity) {
		this.productId = identifier;
		this.quantity = quantity;
		this.updated = LocalDateTime.now();
	}
}
