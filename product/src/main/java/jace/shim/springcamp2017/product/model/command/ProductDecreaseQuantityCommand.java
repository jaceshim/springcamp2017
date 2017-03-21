package jace.shim.springcamp2017.product.model.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductDecreaseQuantityCommand {

	@Setter
	/** 상품 아이디 */
	private Long productId;
	/** 감소수량 */
	private int quantity;

	public ProductDecreaseQuantityCommand() {
	}

	public ProductDecreaseQuantityCommand(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
}
