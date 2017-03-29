package jace.shim.springcamp2017.member.service;

import jace.shim.springcamp2017.member.exception.DuplicateMemberEmailException;
import jace.shim.springcamp2017.member.exception.DuplicateMemberIdException;
import jace.shim.springcamp2017.member.exception.NotExistsMemberException;
import jace.shim.springcamp2017.member.infra.MemberEventHandler;
import jace.shim.springcamp2017.member.model.Member;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 22..
 */
@Service
@Transactional
public class MemberService {

	@Autowired
	private MemberEventHandler eventRepository;

	/**
	 * 회원 등록
	 * @param memberCreateCommand
	 * @return
	 */
	public Member createMember(MemberCommand.CreateMember memberCreateCommand) {
		Member findMember = eventRepository.find(memberCreateCommand.getId());
		if (findMember != null) {
			throw new DuplicateMemberIdException(memberCreateCommand.getId() + " is already registered.");
		}

		if (isDuplicateEmail(memberCreateCommand.getEmail())) {
			throw new DuplicateMemberEmailException(memberCreateCommand.getEmail() + " is already registered.");
		}

		Member member = Member.create(memberCreateCommand);
		// 이벤트 저장
		eventRepository.save(member);

		return member;
	}

	/**
	 * 회원 조회
	 *
	 * @param id
	 * @return
	 */
	public Member getMember(String id) {
		final Member member = eventRepository.find(id);
		if (member == null) {
			throw new NotExistsMemberException(id + "is not exists!");
		}
		return member;
	}

	/**
	 * 회원 명 변경
	 * @param id
	 * @param memberChangeNameCommand
	 * @return
	 */
	public Member changeName(String id, MemberCommand.ChangeName memberChangeNameCommand) {
		final Member member = getMember(id);
		member.changeName(memberChangeNameCommand);

		eventRepository.save(member);

		return member;
	}

	/**
	 * 회원 이메일 변경
	 * @param id
	 * @param memberChangeEmailCommand
	 * @return
	 */
	public Member changeEmail(String id, MemberCommand.ChangeEmail memberChangeEmailCommand) {
		if (isDuplicateEmail(memberChangeEmailCommand.getEmail())) {
			throw new DuplicateMemberEmailException(memberChangeEmailCommand.getEmail() + " is already registered.");
		}

		final Member member = getMember(id);
		member.changeEmail(memberChangeEmailCommand);

		eventRepository.save(member);

		return member;
	}

	/**
	 * 회원 비밀번호 변경
	 * @param id
	 * @param memberChangePasswordCommand
	 * @return
	 */
	public Member changePassword(String id, MemberCommand.ChangePassword memberChangePasswordCommand) {
		final Member member = getMember(id);
		member.changePassword(memberChangePasswordCommand);

		eventRepository.save(member);

		return member;
	}

	/**
	 * 회원 주소 변경
	 * @param id
	 * @param memberChangeAddressCommand
	 * @return
	 */
	public Member changeAddress(String id, MemberCommand.ChangeAddress memberChangeAddressCommand) {
		final Member member = getMember(id);
		member.changeAddress(memberChangeAddressCommand);

		eventRepository.save(member);

		return member;
	}

	/**
	 * 회원 탈퇴여부 변경
	 * @param id
	 * @param memberChangeWithdrawalCommand
	 * @return
	 */
	public Member changeWithdrawal(String id, MemberCommand.ChangeWithdrawal memberChangeWithdrawalCommand) {
		final Member member = getMember(id);
		member.changeWithdrawal(memberChangeWithdrawalCommand);

		eventRepository.save(member);

		return member;
	}

	/**
	 * 이메일 중복 확인
	 * @param email
	 * @return
	 */
	private boolean isDuplicateEmail(String email) {
		final List<Member> members = eventRepository.findAll();
		final long duplicatedEmailCount = members.stream()
			.filter(member -> member.getEmail().equals(email)).count();

		if (duplicatedEmailCount > 0) {
			return true;
		}
		return false;
	}
}
