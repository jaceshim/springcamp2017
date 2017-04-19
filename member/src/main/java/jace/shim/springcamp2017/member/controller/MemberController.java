package jace.shim.springcamp2017.member.controller;

import jace.shim.springcamp2017.member.error.ErrorResource;
import jace.shim.springcamp2017.member.error.FieldErrorResource;
import jace.shim.springcamp2017.member.exception.InvalidRequestException;
import jace.shim.springcamp2017.member.infra.read.MemberReadRepository;
import jace.shim.springcamp2017.member.model.Member;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import jace.shim.springcamp2017.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberReadRepository memberReadRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/members", method = RequestMethod.PUT)
	public ResponseEntity<Member> createMember(@RequestBody @Valid MemberCommand.CreateMember params, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		// String id, String name, String email, String password, String address
		MemberCommand.CreateMember memberCreateCommand = new MemberCommand.CreateMember(
			params.getId(), params.getName(), params.getEmail(),
			passwordEncoder.encode(params.getPassword()), params.getAddress()
		);

		final Member member = memberService.createMember(memberCreateCommand);
		return new ResponseEntity<>(member, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
	public ResponseEntity<jace.shim.springcamp2017.member.model.read.Member> getMember(@PathVariable final String id) {
		final jace.shim.springcamp2017.member.model.read.Member member = memberReadRepository.findById(id);
		return new ResponseEntity<>(member, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST, params = "type=changeProfile")
	public ResponseEntity<Boolean> changeProfile(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangeProfile memberChangeProfileCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		memberService.changeProfile(id, memberChangeProfileCommand);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST, params = "type=changePassword")
	public ResponseEntity<Member> changePassword(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangePassword params, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		MemberCommand.ChangePassword memberChangePasswordCommand = new MemberCommand.ChangePassword(
			passwordEncoder.encode(params.getPassword()), passwordEncoder.encode(params.getNewPassword())
		);
		final Member member = memberService.changePassword(id, memberChangePasswordCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST, params = "type=changeWithdrawal")
	public ResponseEntity<Member> changeWithdrawal(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangeWithdrawal memberChangeWithdrawalCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.changeWithdrawal(id, memberChangeWithdrawalCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity handleInvalidRequest(InvalidRequestException exception) {
		List<FieldError> fieldErrors = exception.getErrors().getFieldErrors();
		List<FieldErrorResource> fieldErrorResources =
			fieldErrors.stream().map(fieldError -> getFieldErrorResource(fieldError)).collect(Collectors.toList());

		ErrorResource error = new ErrorResource("InvalidRequest", exception.getMessage());
		error.setFieldErrors(fieldErrorResources);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity(error, headers, HttpStatus.BAD_REQUEST);
	}

	private FieldErrorResource getFieldErrorResource(FieldError fieldError) {
		FieldErrorResource fieldErrorResource = new FieldErrorResource();
		fieldErrorResource.setResource(fieldError.getObjectName());
		fieldErrorResource.setField(fieldError.getField());
		fieldErrorResource.setCode(fieldError.getCode());
		fieldErrorResource.setMessage(fieldError.getDefaultMessage());
		return fieldErrorResource;
	}

}
