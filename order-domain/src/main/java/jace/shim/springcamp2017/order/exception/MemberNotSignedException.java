package jace.shim.springcamp2017.order.exception;

/**
 * Created by jaceshim on 2017. 4. 18..
 */
public class MemberNotSignedException extends RuntimeException {
	public MemberNotSignedException(String message) {
		super(message);
	}
}
