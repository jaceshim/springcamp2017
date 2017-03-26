package jace.shim.springcamp2017.product.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductPriceChanged extends AbstractProductEvent {
	private int price;
	private LocalDateTime updated;

	public ProductPriceChanged(Long productId, int price) {
		super(productId);
		this.price = price;
		this.updated = LocalDateTime.now();
	}
}
