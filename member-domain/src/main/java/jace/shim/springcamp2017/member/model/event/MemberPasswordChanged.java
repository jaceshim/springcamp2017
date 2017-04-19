package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
@NoArgsConstructor
public class MemberPasswordChanged extends AbstractMemberEvent {
	private String password;
	private String newPassword;
	private LocalDateTime updated;

	public MemberPasswordChanged(String id, String password, String newPassword) {
		this.id = id;
		this.password = password;
		this.newPassword = newPassword;
		this.updated = LocalDateTime.now();
	}
}
