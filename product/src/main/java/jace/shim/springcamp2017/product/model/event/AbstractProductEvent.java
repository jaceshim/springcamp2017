package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public class AbstractProductEvent implements Event<Long> {
	/** 상품 아이디 */
	private Long productId;

	public AbstractProductEvent(Long productId) {
		this.productId = productId;
	}

	@Override
	public Long getIdentifier() {
		return this.productId;
	}
}
