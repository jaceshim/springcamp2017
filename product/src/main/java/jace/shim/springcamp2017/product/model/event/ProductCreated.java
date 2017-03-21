package jace.shim.springcamp2017.product.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 5..
 */
@Getter
public class ProductCreated implements Event {
	/** 상품 아이디 */
	private Long productId;
	/** 상품 명 */
	private String name;
	/** 상품 가격 */
	private int price;
	/** 상품 수량 */
	private int quantity;
	/** 상품 설명 */
	private String description;
	/** 상품 등록일시 */
	private LocalDateTime created;

	public ProductCreated() {
	}

	public ProductCreated(Long productId, String name, int price, int quantity, String description) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
		this.created = LocalDateTime.now();
	}
}
