package jace.shim.springcamp2017.product.model.command;

import jace.shim.springcamp2017.core.command.Command;
import lombok.Getter;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Getter
public class ProductCreateCommand implements Command {
	/** 상품 명 */
	private String name;
	/** 상품 가격 */
	private int price;
	/** 상품 수량 */
	private int quantity;
	/** 상품 설명 */
	private String description;

	public ProductCreateCommand() {
	}

	public ProductCreateCommand(String name, int price, int quantity, String description) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
	}
}
