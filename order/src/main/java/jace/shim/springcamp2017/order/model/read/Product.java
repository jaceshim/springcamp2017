package jace.shim.springcamp2017.order.model.read;

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
	@Column(name="quantity", nullable = false)
	private int quantity;

	/** 상품 설명 */
	@Column(name="description", nullable = false)
	private String description;

	/** 상품 등록일시 */
	@Column(name = "created", nullable = false)
	@Type(type = "jace.shim.springcamp2017.order.infra.LocalDateTimeUserType")
	private LocalDateTime created;

	/** 상품정보 수정일시 */
	@Column(name = "updated", nullable = false)
	@Type(type = "jace.shim.springcamp2017.order.infra.LocalDateTimeUserType")
	private LocalDateTime updated;

	public String getFormatPrice() {
		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(this.price);
	}

}
