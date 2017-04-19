package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Getter
@NoArgsConstructor
public class MemberLogined extends AbstractMemberEvent {

	private LocalDateTime logined;

	public MemberLogined(String id) {
		this.id = id;
		this.logined = LocalDateTime.now();
	}
}
