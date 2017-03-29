package jace.shim.springcamp2017.product.infra;

import jace.shim.springcamp2017.core.event.RawEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 13..
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductRawEvent implements RawEvent<Long> {

	/** seq */
	private Long seq;

	/**  이벤트 식별자 */
	private Long identifier;

	/** 이벤트 유형 */
	private String type;

	/** 이벤트 버전 */
	private Long version;

	/** 이벤트 payload */
	private String payload;

	/** 이벤트 생성일시 */
	private LocalDateTime created;

	public ProductRawEvent(Long identifier, String type, Long version, String payload, LocalDateTime created) {
		this.identifier = identifier;
		this.type = type;
		this.version = version;
		this.payload = payload;
		this.created = created;
	}
}
