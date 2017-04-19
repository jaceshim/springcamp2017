package jace.shim.springcamp2017.member.model.event;

import jace.shim.springcamp2017.core.event.RawEvent;
import jace.shim.springcamp2017.core.infra.InfraConstants;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Entity
@Table(name = "raw_event")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "seq" })
@NoArgsConstructor
public class MemberRawEvent implements RawEvent<String>{
	/** seq */
	@Id
	@Column(name = "seq", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long seq;

	/**  이벤트 식별자 */
	@Column(name = "identifier", nullable = false)
	private String identifier;

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
	@Type(type= InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime created;

	public MemberRawEvent(String identifier, String type, Long version, String payload, LocalDateTime created) {
		this.identifier = identifier;
		this.type = type;
		this.version = version;
		this.payload = payload;
		this.created = created;
	}
}
