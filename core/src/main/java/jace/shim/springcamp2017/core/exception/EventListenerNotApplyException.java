package jace.shim.springcamp2017.core.exception;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
public class EventListenerNotApplyException extends RuntimeException {
	public EventListenerNotApplyException(String message, Exception e) {
		super(message, e);
	}
}
