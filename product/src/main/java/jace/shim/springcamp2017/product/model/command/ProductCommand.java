package jace.shim.springcamp2017.product.model.command;

import jace.shim.springcamp2017.core.command.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jaceshim on 2017. 3. 24..
 */
public final class ProductCommand implements Command {

	private ProductCommand() {}

	@Getter
	@NoArgsConstructor
	public static class CreateProduct {
		/** 상품 아이디 */
		private Long productId;
		/** 상품 명 */
		@NotNull @Size(min = 1)
		private String name;
		/** 상품 가격 */
		@Min(1)
		private int price;
		/** 상품 수량 */
		@Min(1)
		private int quantity;
		/** 상품 설명 */
		private String description;

		public CreateProduct(Long productId, String name, int price, int quantity, String description) {
			this.productId = productId;
			this.name = name;
			this.price = price;
			this.quantity = quantity;
			this.description = description;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangeName {
		@NotNull @Size(min = 1)
		/** 상품 명 */
		private String name;

		public ChangeName(String name) {
			this.name = name;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangePrice {
		@Min(1)
		/** 상품 가격 */
		private int price;

		public ChangePrice(int price) {
			this.price = price;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class DecreaseQuantity {
		@Min(1)
		/** 감소수량 */
		private int quantity;

		public DecreaseQuantity(int quantity) {
			this.quantity = quantity;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class IncreaseQuantity {
		@Min(1)
		/** 감소수량 */
		private int quantity;

		public IncreaseQuantity(int quantity) {
			this.quantity = quantity;
		}
	}
}
