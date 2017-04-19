package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 4. 15..
 */
@Getter
@NoArgsConstructor
public class MemberProfileChanged extends AbstractMemberEvent {
	private String id;
	private String name;
	private String email;
	private String address;
	private LocalDateTime updated;

	public MemberProfileChanged(String id, String name, String email, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.updated = LocalDateTime.now();
	}
}
