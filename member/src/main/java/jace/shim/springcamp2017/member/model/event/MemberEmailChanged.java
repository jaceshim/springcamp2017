package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public class MemberEmailChanged extends AbstractMemberEvent {
	private String id;
	private String email;
	private LocalDateTime updated;

	public MemberEmailChanged(String id, String email) {
		super(id);
		this.id = id;
		this.email = email;
		this.updated = LocalDateTime.now();
	}
}
