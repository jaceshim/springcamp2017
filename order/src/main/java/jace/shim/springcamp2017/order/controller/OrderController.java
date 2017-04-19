package jace.shim.springcamp2017.order.controller;

import jace.shim.springcamp2017.order.error.ErrorResource;
import jace.shim.springcamp2017.order.error.FieldErrorResource;
import jace.shim.springcamp2017.order.model.command.OrderCommand;
import jace.shim.springcamp2017.order.model.read.Order;
import jace.shim.springcamp2017.order.service.OrderService;
import jace.shim.springcamp2017.order.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderCommand.CreateOrder orderCreateCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final jace.shim.springcamp2017.order.model.Order order = orderService.createProduct(orderCreateCommand);

		return new ResponseEntity<>(new Order(order.getOrderId()), HttpStatus.CREATED);
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
