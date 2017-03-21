package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Getter
public class ProductNameChanged implements Event {

	private Long productId;

	private String name;

	private LocalDateTime updated;

	public ProductNameChanged() {
	}

	public ProductNameChanged(Long productId, String name) {
		this.productId = productId;
		this.name = name;
		this.updated = LocalDateTime.now();
	}
}
