package jace.shim.springcamp2017.product.exception;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public class NotExistsProductException extends RuntimeException {
	public NotExistsProductException(String message) {
		super(message);
	}
}
