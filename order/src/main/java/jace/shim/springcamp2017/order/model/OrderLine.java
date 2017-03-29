package jace.shim.springcamp2017.order.model;

import jace.shim.springcamp2017.order.model.read.Product;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
public class OrderLine {

	/** 주문 상품 정보 */
	private Product product;

	/** 주문수량 */
	private int quantity;

	public OrderLine(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
}
