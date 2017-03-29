package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 27..
 */
@Getter
@NoArgsConstructor
public class MemberWithdrawalChanged extends AbstractMemberEvent {
	private boolean withdrawal;
	private LocalDateTime updated;

	public MemberWithdrawalChanged(String id, boolean withdrawal) {
		this.id = id;
		this.withdrawal = withdrawal;
		this.updated = LocalDateTime.now();
	}
}

