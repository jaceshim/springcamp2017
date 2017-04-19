package jace.shim.springcamp2017.member.model.read;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jaceshim on 2017. 4. 15..
 */
@Getter
@NoArgsConstructor
public class MemberAuthToken {
	private String id;
	private String token;

	public MemberAuthToken(String id, String token) {
		this.id = id;
		this.token = token;
	}
}
