package jace.shim.springcamp2017.member.model.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public class MemberAddressChanged extends AbstractMemberEvent {
	private String address;
	private LocalDateTime updated;

	public MemberAddressChanged(String id, String address) {
		super(id);
		this.address = address;
		this.updated = LocalDateTime.now();
	}
}
