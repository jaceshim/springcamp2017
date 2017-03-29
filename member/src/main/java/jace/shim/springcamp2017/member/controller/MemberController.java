package jace.shim.springcamp2017.member.controller;

import jace.shim.springcamp2017.member.error.ErrorResource;
import jace.shim.springcamp2017.member.error.FieldErrorResource;
import jace.shim.springcamp2017.member.error.InvalidRequestException;
import jace.shim.springcamp2017.member.model.Member;
import jace.shim.springcamp2017.member.model.command.MemberCommand;
import jace.shim.springcamp2017.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/members", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Member> createMember(@RequestBody @Valid MemberCommand.CreateMember memberCreateCommand, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.createMember(memberCreateCommand);
		return new ResponseEntity<>(member, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST,
		params = "type=changeName",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Member> changeName(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangeName memberChangeNameCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.changeName(id, memberChangeNameCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST,
		params = "type=changeEmail",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Member> changeEmail(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangeEmail memberChangeEmailCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.changeEmail(id, memberChangeEmailCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST,
		params = "type=changePassword",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Member> changePassword(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangePassword memberChangePasswordCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.changePassword(id, memberChangePasswordCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST,
		params = "type=changeAddress",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Member> changeAddress(@PathVariable final String id,
		@RequestBody @Valid MemberCommand.ChangeAddress memberChangeAddressCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Member member = memberService.changeAddress(id, memberChangeAddressCommand);

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST,
		params = "type=changeWithdrawal",
		produces = MediaType.APPLICATION_JSON_VALUE)
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
