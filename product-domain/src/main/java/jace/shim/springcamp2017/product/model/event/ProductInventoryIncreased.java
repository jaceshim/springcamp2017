package jace.shim.springcamp2017.product.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
@NoArgsConstructor
public class ProductInventoryIncreased extends AbstractProductEvent {
	private int inventory;
	private LocalDateTime updated;

	public ProductInventoryIncreased(Long productId, int inventory) {
		this.productId = productId;
		this.inventory = inventory;
		this.updated = LocalDateTime.now();
	}
}
