package jace.shim.springcamp2017.product.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductQuantityDecreased extends AbstractProductEvent {
	private int quantity;
	private LocalDateTime updated;

	public ProductQuantityDecreased(Long productId, int quantity) {
		super(productId);
		this.quantity = quantity;
		this.updated = LocalDateTime.now();
	}
}
