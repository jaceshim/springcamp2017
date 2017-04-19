package jace.shim.springcamp2017.member.model.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jace.shim.springcamp2017.core.infra.InfraConstants;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 30..
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "id" })
@JsonIgnoreProperties({ "password"})
@NoArgsConstructor
public class Member implements Serializable {
	/** seq */
	@Id
	@Column(name = "seq", nullable = false)
	private Long seq;

	/**  회원 아이디*/
	@Column(name = "id", nullable = false)
	private String id;

	/** 회원 명 */
	@Column(name = "name", nullable = false)
	private String name;

	/** 회원 이메일 */
	@Column(name="email", nullable = false)
	private String email;

	/** 회원 비밀번호 */
	@Column(name="password", nullable = false)
	private String password;

	/** 회원 탈퇴여부 */
	@Column(name="withdrawal", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean withdrawal;

	/** 회원 주소 */
	@Column(name="address", nullable = false)
	private String address;

	/** 회원 등록일시 */
	@Column(name = "created", nullable = false)
	@Type(type = InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime created;

	/** 회원정보 수정일시 */
	@Column(name = "updated", nullable = false)
	@Type(type = InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime updated;

	/** 회원로그인 일시 */
	@Column(name = "logined", nullable = false)
	@Type(type = InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime logined;

	/** 회원로그아웃 일시 */
	@Column(name = "logouted", nullable = false)
	@Type(type = InfraConstants.LOCAL_DATE_TIME_TYPE)
	private LocalDateTime logouted;

	public Member(String id, String name, String email, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
	}
}
