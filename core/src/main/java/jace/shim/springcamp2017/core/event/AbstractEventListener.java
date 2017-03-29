package jace.shim.springcamp2017.core.event;

import jace.shim.springcamp2017.core.exception.EventListenerNotApplyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
public abstract class AbstractEventListener implements EventListener {

	protected static String APPLY_METHOD_NAME = "apply";

	@Override
	public void handle(Event event) {
		Method method;
		try {
			method = this.getClass().getDeclaredMethod(APPLY_METHOD_NAME, event.getClass());
			if (method != null) {
				method.setAccessible(true);
				method.invoke(this, event);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			throw new EventListenerNotApplyException(e.getMessage(), e);
		}
	}
}


