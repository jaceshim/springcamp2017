package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@Getter
@NoArgsConstructor
public class MemberCreated extends AbstractMemberEvent {
	private String name;
	private String email;
	private String password;
	private String address;
	private LocalDateTime created;

	public MemberCreated(String id, String name, String email, String password, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.created = LocalDateTime.now();
	}
}
