package jace.shim.springcamp2017.member.model.command;

import jace.shim.springcamp2017.core.command.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jaceshim on 2017. 3. 24..
 */
public final class MemberCommand implements Command {

	private MemberCommand() {}

	@Getter
	@NoArgsConstructor
	public static class CreateMember {
		/** 회원 아이디 */
		@NotNull
		@Size(min=4, max=8)
		private String id;

		/** 회원 명 */
		@NotNull
		@Size(min=3, max=10)
		private String name;

		/** 회원 이메일 */
		@NotNull
		@Size(max=50)
		private String email;

		/** 회원 비밀번호 */
		@NotNull
		@Size(min=4, max=8)
		private String password;

		/** 회원 주소 */
		@NotNull
		@Size(min=10)
		private String address;

		public CreateMember(String id, String name, String email, String password, String address) {
			this.id = id;
			this.name = name;
			this.email = email;
			this.password = password;
			this.address = address;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangeName {
		/** 회원 명 */
		@NotNull
		@Size(min=3, max=10)
		private String name;

		public ChangeName(String name) {
			this.name = name;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangeEmail {
		/** 회원 이메일 */
		@NotNull
		@Size(max=50)
		private String email;

		public ChangeEmail(String email) {
			this.email = email;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangeAddress {
		/** 회원 주소 */
		@NotNull
		@Size(min=10)
		private String address;

		public ChangeAddress(String address) {
			this.address = address;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangePassword {
		/** 회원 비밀번호 */
		@NotNull
		@Size(min=4, max=8)
		private String password;

		/** 회원 신귭 비밀번호 */
		@NotNull
		@Size(min=4, max=8)
		private String newPassword;

		public ChangePassword(String password, String newPassword) {
			this.password = password;
			this.newPassword = newPassword;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ChangeWithdrawal {
		/**
		 * 회원 탈퇴 여부
		 */
		private boolean withdrawal;

		public ChangeWithdrawal(boolean withdrawal) {
			this.withdrawal = withdrawal;
		}
	}
}
