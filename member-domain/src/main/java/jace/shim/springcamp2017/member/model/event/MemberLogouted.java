package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Getter
@NoArgsConstructor
public class MemberLogouted extends AbstractMemberEvent {
	private LocalDateTime logouted;

	public MemberLogouted(String id) {
		this.id = id;
		this.logouted = LocalDateTime.now();
	}

}
