package jace.shim.springcamp2017.product.infra;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 13..
 */
@Entity
@Table(name = "raw_event")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "seq" })
@NoArgsConstructor
public class ProductRawEvent {

	/** seq */
	@Id
	@Column(name = "seq", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long seq;

	/**  이벤트 식별자 */
	@Column(name = "identifer", nullable = false)
	private Long identifer;

	/** 이벤트 유형 */
	@Column(name = "type", nullable = false)
	private String type;

	/** 이벤트 버전 */
	@Column(name="version", nullable = false)
	private Long version;

	/** 이벤트 payload */
	@Column(name="payload", nullable = false)
	private String payload;

	/** 이벤트 생성일시 */
	@Column(name = "created", nullable = false)
	@Type(type="jace.shim.springcamp2017.product.infra.LocalDateTimeUserType")
	private LocalDateTime created;

	public ProductRawEvent(Long identifer, String type, Long version, String payload, LocalDateTime created) {
		this.identifer = identifer;
		this.type = type;
		this.version = version;
		this.payload = payload;
		this.created = created;
	}
}
