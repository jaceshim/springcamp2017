package jace.shim.springcamp2017.product.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
@NoArgsConstructor
public class ProductQuantityIncreased extends AbstractProductEvent {
	private int quantity;
	private LocalDateTime updated;

	public ProductQuantityIncreased(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
		this.updated = LocalDateTime.now();
	}
}
