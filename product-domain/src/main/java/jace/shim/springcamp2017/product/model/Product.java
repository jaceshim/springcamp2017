package jace.shim.springcamp2017.product.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.core.exception.EventApplyException;
import jace.shim.springcamp2017.product.exception.InsufficientInventoryQuantityException;
import jace.shim.springcamp2017.product.model.command.ProductCommand;
import jace.shim.springcamp2017.product.model.event.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Getter
public class Product extends AggregateRoot<Long> {
	/** 상품 아이디 */
	private Long productId;
	/** 상품 명 */
	private String name;
	/** 상품 가격 */
	private int price;
	/** 상품 수량 */
	private int inventory;
	/** 상품 이미지 경로 */
	private String imagePath;
	/** 상품 설명 */
	private String description;
	/** 상품 등록일시 */
	private LocalDateTime created;
	/** 상품 수정일시 */
	private LocalDateTime updated;

	Product() {
		super();
	}

	Product(Long productId) {
		super(productId);
		this.productId = productId;
	}

	Product(Long productId, String name, int price, int quantity, String imagePath, String description) {
		super(productId);
		applyChange(new ProductCreated(productId, name, price, quantity, imagePath, description));
	}

	public Product(Long productId, String name, int price, int inventory) {
		super(productId);
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
	}

	/**
	 * 상품 등록
	 *
	 * @param productCreateCommand
	 * @return
	 * @throws EventApplyException
	 */
	public static Product create(Long productId, ProductCommand.CreateProduct productCreateCommand) {
		Product product = new Product(productId, productCreateCommand.getName(), productCreateCommand.getPrice(),
			productCreateCommand.getQuantity(), productCreateCommand.getImagePath(),
			productCreateCommand.getDescription());

		return product;
	}

	/**
	 * 상품아이디 생성
	 * @return
	 */
	private static long createProductId() {
		long min = 1000000L;
		long max = 9999999L;
		return min + (long) (Math.random() * (max - min));
	}

	/**
	 * 상품생성 이벤트 반영
	 *
	 * @param event
	 */
	public void apply(ProductCreated event) {
		this.productId = event.getProductId();
		this.name = event.getName();
		this.price = event.getPrice();
		this.inventory = event.getInventory();
		this.imagePath = event.getImagePath();
		this.description = event.getDescription();
		this.created = event.getCreated();
	}

	/**
	 * 상품명 변경
	 *
	 * @param productChangeNameCommand
	 * @throws EventApplyException
	 */
	public void changeName(ProductCommand.ChangeName productChangeNameCommand) {
		applyChange(new ProductNameChanged(this.getProductId(), productChangeNameCommand.getName()));
	}

	/**
	 * 상품명 변경 이벤트 반영
	 * @param event
	 */
	public void apply(ProductNameChanged event) {
		this.name = event.getName();
		this.updated = event.getUpdated();
	}

	/**
	 * 상품 가격 변경
	 * @param productChangePriceCommand
	 */
	public void changePrice(ProductCommand.ChangePrice productChangePriceCommand) {
		applyChange(new ProductPriceChanged(this.getProductId(), productChangePriceCommand.getPrice()));
	}

	/**
	 * 상품 가격 변경 이벤트 반영
	 * @param event
	 */
	public void apply(ProductPriceChanged event) {
		this.price = event.getPrice();
		this.updated = event.getUpdated();
	}

	/**
	 * 상품 판매수량 증가
	 * @param productIncreaseQuantityCommand
	 * @throws EventApplyException
	 */
	public void increaseQuantity(ProductCommand.IncreaseQuantity productIncreaseQuantityCommand) {
		applyChange(new ProductInventoryIncreased(this.getProductId(), productIncreaseQuantityCommand.getQuantity()));
	}

	/**
	 * 상품 판매수량 증가 이벤트 반영
	 * @param event
	 */
	public void apply(ProductInventoryIncreased event) {
		this.inventory = this.getInventory() + event.getInventory();
		this.updated = event.getUpdated();
	}

	/**
	 * 상품 판매수량 감소
	 * @param productDecreaseQuantityCommand
	 * @throws EventApplyException
	 */
	public void decreaseQuantity(ProductCommand.DecreaseQuantity productDecreaseQuantityCommand) {
		if (this.getInventory() < productDecreaseQuantityCommand.getQuantity()) {
			throw new InsufficientInventoryQuantityException(this.getProductId() + " is not insufficient inventory quantity");
		}
		applyChange(new ProductInventoryDecreased(this.getProductId(), productDecreaseQuantityCommand.getQuantity()));
	}

	/**
	 * 상품 판매수량 감소 이벤트 반영
	 * @param event
	 */
	public void apply(ProductInventoryDecreased event) {
		this.inventory = this.getInventory() - event.getInventory();
		this.updated = event.getUpdated();
	}
}
