package jace.shim.springcamp2017.order.model.read;

import jace.shim.springcamp2017.product.model.read.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

	private Long productId;
	/** 주문 상품 정보 */
	private Product product;

	private int price;

	/** 주문수량 */
	private int quantity;

	public OrderItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public OrderItem(Long productId, int price, int quantity) {
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
	}

	public int getProductPrice() {
		return price * quantity;
	}

	private static DecimalFormat PRICE_DECIMAL_FORMAT = new DecimalFormat("#,##0");
	public String getFormatProductPrice() {
		return PRICE_DECIMAL_FORMAT.format(this.getProductPrice());
	}
}
