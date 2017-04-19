package jace.shim.springcamp2017.member.model;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.member.exception.NotMatchPasswordException;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import jace.shim.springcamp2017.member.model.event.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
	private LocalDateTime logined;
	/** 로그아웃일시 */
	private LocalDateTime logouted;

	Member() {
		super();
	}

	Member(String id) {
		super(id);
		this.id = id;
	}

	Member(String id, String name, String email, String password, String address) {
		super(id);
		applyChange(new MemberCreated(id, name, email, password, address));
	}

	public Member(String id, String name, String email, String address) {
		super(id);
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
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
	 * 회원 비밀번호 변경
	 * @param memberChangePasswordCommand
	 */
	public void changePassword(MemberCommand.ChangePassword memberChangePasswordCommand) {
		if (!this.getPassword().equals(memberChangePasswordCommand.getPassword())) {
			throw new NotMatchPasswordException("memer id : " + this.getId() + " is not match password!");
		}

		applyChange(new MemberPasswordChanged(this.getId(), memberChangePasswordCommand.getPassword(), memberChangePasswordCommand.getNewPassword()));
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
	 * 회원정보 변경
	 * @param memberChangeProfileCommand
	 */
	public void changeProfile(MemberCommand.ChangeProfile memberChangeProfileCommand) {
		applyChange(new MemberProfileChanged(this.getId(), memberChangeProfileCommand.getName(), memberChangeProfileCommand.getEmail(),
			memberChangeProfileCommand.getAddress()));
	}

	/**
	 * 회원정보 변경 이벤트 반영
	 * @param event
	 */
	public void apply(MemberProfileChanged event) {
		this.name = event.getName();
		this.email = event.getEmail();
		this.address = event.getAddress();
	}

	/**
	 * 회원 로그인
	 */
	public void login() {
		applyChange(new MemberLogined(this.getId()));
	}

	/**
	 * 회원 로그인 이벤트 반영
	 * @param event
	 */
	public void apply(MemberLogined event) {
		this.logined = event.getLogined();
	}

	/**
	 * 회원 로그아웃
	 */
	public void logout() {
		applyChange(new MemberLogouted(this.getId()));
	}

	/**
	 * 회원 로그아웃 이벤트 반영
	 * @param event
	 */
	public void apply(MemberLogouted event) {
		this.logouted = event.getLogouted();
	}
}
