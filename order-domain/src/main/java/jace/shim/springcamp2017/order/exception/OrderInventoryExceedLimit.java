package jace.shim.springcamp2017.order.exception;

import static java.lang.String.format;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
public class OrderInventoryExceedLimit extends RuntimeException {
	public OrderInventoryExceedLimit(Long productId, int quantity) {
		super(format("상품 %d의 재고수량이 부족합니다. 주문수량: %d", productId, quantity));
	}
}
