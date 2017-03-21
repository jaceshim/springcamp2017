package jace.shim.springcamp2017.product.model.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductIncreaseQuantityCommand {
	@Setter
	/** 상품 아이디 */
	private Long productId;
	/** 증가 수량 */
	private int quantity;

	public ProductIncreaseQuantityCommand() {
	}

	public ProductIncreaseQuantityCommand(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
}
