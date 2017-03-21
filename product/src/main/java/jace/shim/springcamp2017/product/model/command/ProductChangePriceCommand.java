package jace.shim.springcamp2017.product.model.command;

import jace.shim.springcamp2017.core.command.Command;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Getter
public class ProductChangePriceCommand implements Command {
	@Setter
	/** 상품 아이디 */
	private Long productId;
	/** 변경 상품 가격 */
	private int price;

	public ProductChangePriceCommand() {
	}

	public ProductChangePriceCommand(Long productId, int price) {
		this.productId = productId;
		this.price = price;
	}
}
