package jace.shim.springcamp2017.product.model.read;

import jace.shim.springcamp2017.core.infra.InfraConstants;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "productId" })
@NoArgsConstructor
public class Product {

	/** seq */
	@Id
	@Column(name = "seq", nullable = false)
	private Long seq;

	/**  상품 아이디*/
	@Column(name = "productId", nullable = false)
	private Long productId;

	/** 상품 명 */
	@Column(name = "name", nullable = false)
	private String name;

	/** 상품 가격 */
	@Column(name="price", nullable = false)
	private int price;

	/** 상품 수량 */
	@Column(name="inventory", nullable = false)
	private int inventory;

	/**  상품 이미지 경로 */
	@Column(name="imagePath", nullable = false)
	private String imagePath;

	/** 상품 설명 */
	@Column(name="description", nullable = false)
	private String description;

	/** 상품 등록일시 */
	@Column(name = "created", nullable = false)
	@Type(type= InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime created;

	/** 상품정보 수정일시 */
	@Column(name = "updated", nullable = false)
	@Type(type= InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime updated;

	public Product(Long productId, String name, int price, int inventory) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
	}

	public Product(Long productId, String name, int price, String imagePath) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.imagePath = imagePath;
	}

	public String getFormatPrice() {
		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(this.price);
	}

}