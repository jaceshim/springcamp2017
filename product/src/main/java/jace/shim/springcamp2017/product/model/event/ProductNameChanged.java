package jace.shim.springcamp2017.product.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Getter
public class ProductNameChanged extends AbstractProductEvent {
	private String name;
	private LocalDateTime updated;

	public ProductNameChanged(Long productId, String name) {
		super(productId);
		this.name = name;
		this.updated = LocalDateTime.now();
	}
}
