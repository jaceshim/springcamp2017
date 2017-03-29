package jace.shim.springcamp2017.member.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.member.exception.NotMatchPasswordException;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import jace.shim.springcamp2017.member.model.event.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

	public Member(String id) {
		super(id);
		this.id = id;
	}

	public Member(String id, String name, String email, String password, String address) {
		super(id);
		applyChange(new MemberCreated(id, name, email, encrypt(password), address));
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

	/**
	 * 회원 등록 이벤트 반영
	 * @param event
	 */
	public void apply(MemberCreated event) {
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
		this.password = event.getPassword();
		this.address = event.getAddress();
		this.created = LocalDateTime.now();
	}

	/**
	 * 회원 명 변경
	 *
	 * @param memberChangeNameCommand
	 */
	public void changeName(MemberCommand.ChangeName memberChangeNameCommand) {
		applyChange(new MemberNameChanged(this.getId(), memberChangeNameCommand.getName()));
	}

	/**
	 * 회원 명 변경 이벤트 반영
	 * @param event
	 */
	public void apply(MemberNameChanged event) {
		this.name = event.getName();
		this.updated = event.getUpdated();
	}

	/**
	 * 회원 이메일 변경
	 * @param memberChangeEmailCommand
	 */
	public void changeEmail(MemberCommand.ChangeEmail memberChangeEmailCommand) {
		applyChange(new MemberEmailChanged(this.getId(), memberChangeEmailCommand.getEmail()));
	}

	/**
	 * 회원 이메일 변경 이벤트 반영
	 * @param event
	 */
	public void apply(MemberEmailChanged event) {
		this.email = event.getEmail();
		this.updated = event.getUpdated();
	}

	/**
	 * 회원 비밀번호 변경
	 * @param memberChangePasswordCommand
	 */
	public void changePassword(MemberCommand.ChangePassword memberChangePasswordCommand) {
		if (!this.getPassword().equals(encrypt(memberChangePasswordCommand.getPassword()))) {
			throw new NotMatchPasswordException("memer id : " + this.getId() + " is not match password!");
		}

		String encryptPassword = encrypt(memberChangePasswordCommand.getPassword());
		String encryptNewPassword = encrypt(memberChangePasswordCommand.getNewPassword());

		applyChange(new MemberPasswordChanged(this.getId(), encryptPassword, encryptNewPassword));
	}

	/**
	 * 회원 비밀번호 변경 이벤트 반영
	 * @param event
	 */
	public void apply(MemberPasswordChanged event) {
		this.password = event.getNewPassword();
		this.updated = event.getUpdated();
	}

	/**
	 * 회원 주소 변경
	 * @param memberChangeAddressCommand
	 */
	public void changeAddress(MemberCommand.ChangeAddress memberChangeAddressCommand) {
		applyChange(new MemberAddressChanged(this.getId(), memberChangeAddressCommand.getAddress()));
	}

	/**
	 * 회원 주소 변경 이벤트 반영
	 * @param event
	 */
	public void apply(MemberAddressChanged event) {
		this.address = event.getAddress();
		this.updated = event.getUpdated();
	}

	/**
	 * 회원 탈퇴 여부 변경
	 * @param memberChangeWithdrawalCommand
	 */
	public void changeWithdrawal(MemberCommand.ChangeWithdrawal memberChangeWithdrawalCommand) {
		applyChange(new MemberWithdrawalChanged(this.getId(), memberChangeWithdrawalCommand.isWithdrawal()));
	}

	/**
	 * 회원 탈퇴 여부 변경 반영
	 * @param event
	 */
	public void apply(MemberWithdrawalChanged event) {
		this.withdrawal = event.isWithdrawal();
		this.updated = event.getUpdated();
	}

	/**
	 * Simeple Hash Encrypt
	 * @param input
	 * @return
	 */
	private String encrypt(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		}
		md.update(input.getBytes());
		byte byteData[] = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
