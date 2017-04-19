package jace.shim.springcamp2017.member.service;

import jace.shim.springcamp2017.member.model.read.Member;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by jaceshim on 2017. 4. 15..
 */
public class LoginMemberDetails extends org.springframework.security.core.userdetails.User {

	private final Member member;

	public LoginMemberDetails(Member member){
		super(
			member.getId(),
			member.getPassword(),
			AuthorityUtils.createAuthorityList("ROLE_USER")
		);
		this.member = member;
	}
}