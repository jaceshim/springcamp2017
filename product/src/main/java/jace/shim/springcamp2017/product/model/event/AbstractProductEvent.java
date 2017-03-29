package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public class AbstractProductEvent implements Event<Long> {
	/** 상품 아이디 */
	protected Long productId;

	@Override
	public Long getIdentifier() {
		return this.productId;
	}
}
