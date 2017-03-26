package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public class MemberNameChanged extends AbstractMemberEvent {
	private String name;
	private LocalDateTime updated;

	public MemberNameChanged(String id, String name) {
		super(id);
		this.name = name;
		this.updated = LocalDateTime.now();
	}
}

