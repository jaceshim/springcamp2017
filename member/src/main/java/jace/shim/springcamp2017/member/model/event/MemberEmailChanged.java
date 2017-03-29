package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
@NoArgsConstructor
public class MemberEmailChanged extends AbstractMemberEvent {
	private String email;
	private LocalDateTime updated;

	public MemberEmailChanged(String id, String email) {
		this.id = id;
		this.email = email;
		this.updated = LocalDateTime.now();
	}
}
