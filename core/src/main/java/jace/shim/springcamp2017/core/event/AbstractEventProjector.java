package jace.shim.springcamp2017.core.event;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Slf4j
public abstract class AbstractEventProjector implements EventProjector {

	protected static String APPLY_METHOD_NAME = "execute";

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
			log.warn(e.getMessage(), e);
			//throw new EventListenerNotApplyException(e.getMessage(), e);
		}
	}

	protected Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
