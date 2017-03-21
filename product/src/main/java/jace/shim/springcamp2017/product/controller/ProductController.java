package jace.shim.springcamp2017.product.controller;

import jace.shim.springcamp2017.product.model.Product;
import jace.shim.springcamp2017.product.model.command.*;
import jace.shim.springcamp2017.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Long> add(@RequestBody ProductCreateCommand productCreateCommand) throws Exception {
		final Long identifier = productService.add(productCreateCommand);
		return new ResponseEntity<>(identifier, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=changeName",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> changeName(@PathVariable final Long productId,
		@RequestBody ProductChangeNameCommand productChangeNameCommand) throws Exception {
		final Product product = productService.changeName(productChangeNameCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=changePrice",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> changePrice(@PathVariable final Long productId,
		@RequestBody ProductChangePriceCommand productChangePriceCommand) throws Exception {
		productChangePriceCommand.setProductId(productId);
		final Product product = productService.changePrice(productChangePriceCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=increaseQuantity",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> increaseQuantity(@PathVariable final Long productId,
		@RequestBody ProductIncreaseQuantityCommand productIncreaseQuantityCommand) throws Exception {
		productIncreaseQuantityCommand.setProductId(productId);
		final Product product = productService.increaseQuantity(productIncreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST,
		params = "type=decreaseQuantity",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> decreaseQuantity(@PathVariable final Long productId,
		@RequestBody ProductDecreaseQuantityCommand productDecreaseQuantityCommand) throws Exception {
		productDecreaseQuantityCommand.setProductId(productId);
		final Product product = productService.decreaseQuantity(productDecreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Product> getProduct(@PathVariable final Long productId) throws Exception {
		final Product product = productService.getProduct(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

}
