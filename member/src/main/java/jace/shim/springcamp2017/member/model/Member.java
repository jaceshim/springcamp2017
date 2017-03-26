package jace.shim.springcamp2017.member.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.member.exception.NotMatchPasswordException;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import jace.shim.springcamp2017.member.model.event.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 22..
 */
@Getter
@Slf4j
public class Member extends AggregateRoot<String> {

	/** 회원 아이디 */
	private String id;
	/** 회원 명 */
	private String name;
	/** 회원 이메일 */
	private String email;
	/** 회원 비밀번호 */
	private String password;
	/** 회원 주소 */
	private String address;
	/** 회원 탈퇴여부 */
	private boolean withdrawal;
	/** 가입일시 */
	private LocalDateTime created;
	/** 수정일시 */
	private LocalDateTime updated;
	/** 로그인일시 */
	private LocalDateTime logged;

	public Member(String id, String name, String email, String password, String address) {
		super(id);
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = encrypt(password);
		this.address = address;
		this.withdrawal = false;

		applyChange(new MemberCreated(id, name, email, password, address));
	}

	/**
	 * 회원 등록
	 *
	 * @param memberCreateCommand
	 * @return
	 */
	public static Member create(MemberCommand.CreateMember memberCreateCommand) {
		Member member = new Member(memberCreateCommand.getId(),
			memberCreateCommand.getName(),
			memberCreateCommand.getEmail(),
			memberCreateCommand.getPassword(),
			memberCreateCommand.getAddress());

		return member;
	}

	public void apply(MemberCreated event) {
		this.id = event.getIdentifier();
		this.name = event.getName();
		this.email = event.getEmail();
		this.password = event.getPassword();
		this.address = event.getAddress();
		this.created = LocalDateTime.now();
	}

	private String encrypt(String input) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			byte[] buffer = new byte[0];
			buffer = input.getBytes("UTF-8");

			md.update(buffer);
			byte[] digest = md.digest();

			String hexStr = "";
			for (int i = 0; i < digest.length; i++) {
				hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 회원 명 변경
	 *
	 * @param memberChangeNameCommand
	 */
	public void changeName(MemberCommand.ChangeName memberChangeNameCommand) {
		this.name = memberChangeNameCommand.getName();
		applyChange(new MemberNameChanged(this.id, this.getName()));
	}

	public void apply(MemberNameChanged event) {
		this.name = event.getName();
	}

	/**
	 * 회원 이메일 변경
	 * @param memberChangeEmailCommand
	 */
	public void changeEmail(MemberCommand.ChangeEmail memberChangeEmailCommand) {
		this.email = memberChangeEmailCommand.getEmail();
		applyChange(new MemberEmailChanged(this.id, this.getName()));
	}

	public void apply(MemberEmailChanged event) {
		this.email = event.getEmail();
	}

	/**
	 * 회원 비밀번호 변경
	 * @param memberChangePasswordCommand
	 */
	public void changePassword(MemberCommand.ChangePassword memberChangePasswordCommand) {
		if (!memberChangePasswordCommand.getPassword().equals(memberChangePasswordCommand.getNewPassword())) {
			throw new NotMatchPasswordException(this.id + " is not match password!");
		}
		this.password = encrypt(memberChangePasswordCommand.getNewPassword());
		applyChange(new MemberPasswordChanged(this.id, memberChangePasswordCommand.getPassword(), memberChangePasswordCommand.getNewPassword()));
	}

	public void apply(MemberPasswordChanged event) {
		this.password = event.getNewPassword();
	}

	/**
	 * 회원 주소 변경
	 * @param memberChangeAddressCommand
	 */
	public void changeAddress(MemberCommand.ChangeAddress memberChangeAddressCommand) {
		this.address = memberChangeAddressCommand.getAddress();
		applyChange(new MemberAddressChanged(this.id, memberChangeAddressCommand.getAddress()));
	}

	public void apply(MemberAddressChanged event) {
		this.address = event.getAddress();
	}
}
