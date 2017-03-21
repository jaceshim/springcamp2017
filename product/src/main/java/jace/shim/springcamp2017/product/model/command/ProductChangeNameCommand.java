package jace.shim.springcamp2017.product.model.command;

import jace.shim.springcamp2017.core.command.Command;
import lombok.Getter;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Getter
public class ProductChangeNameCommand implements Command {
	/** 상품 아이디 */
	private Long productId;
	/** 상품 명 */
	private String name;

	public ProductChangeNameCommand() {
	}

	public ProductChangeNameCommand(Long productId, String name) {
		this.productId = productId;
		this.name = name;
	}
}
