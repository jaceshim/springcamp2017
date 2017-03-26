package jace.shim.springcamp2017.product.controller;

import jace.shim.springcamp2017.product.error.ErrorResource;
import jace.shim.springcamp2017.product.error.FieldErrorResource;
import jace.shim.springcamp2017.product.error.InvalidRequestException;
import jace.shim.springcamp2017.product.model.Product;
import jace.shim.springcamp2017.product.model.command.ProductCommand;
import jace.shim.springcamp2017.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
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
 * Created by jaceshim on 2017. 3. 3..
 */
@RestController
@Slf4j
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/products", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCommand.CreateProduct productCreateCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.createProduct(productCreateCommand);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=changeName",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> changeName(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.ChangeName productChangeNameCommand, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.changeName(productId, productChangeNameCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=changePrice",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> changePrice(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.ChangePrice productChangePriceCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.changePrice(productId, productChangePriceCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=increaseQuantity",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> increaseQuantity(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.IncreaseQuantity productIncreaseQuantityCommand, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.increaseQuantity(productId, productIncreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=decreaseQuantity",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> decreaseQuantity(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.DecreaseQuantity productDecreaseQuantityCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.decreaseQuantity(productId, productDecreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Product> getProduct(@PathVariable final Long productId) {
		final Product product = productService.getProduct(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
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
