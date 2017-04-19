package jace.shim.springcamp2017.product.exception;

/**
 * Created by jaceshim on 2017. 4. 18..
 */
public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(String message) {
		super(message);
	}
}
