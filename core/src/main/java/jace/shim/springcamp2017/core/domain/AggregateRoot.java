package jace.shim.springcamp2017.core.domain;

import com.google.common.collect.Lists;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.exception.EventApplyException;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 5..
 */
@Slf4j
public abstract class AggregateRoot<ID> implements Serializable {

	private ID identifier;

	private List<Event> changeEvents = Lists.newArrayList();

	private Long expectedVersion = 0L;

	public AggregateRoot(ID identifier) {
		this.identifier = identifier;
	}

	public void markChangesAsCommitted() {
		this.changeEvents.clear();
	}

	public List<Event> getUncommittedChanges() {
		return this.changeEvents;
	}

	public ID getIdentifier() {
		return identifier;
	}

	public Long getExpectedVersion() {
		return expectedVersion;
	}

	public void replay(List<Event> changes) throws EventApplyException {
		for (Event change : changes) {
			applyChange(change, false);
		}
	}

	protected void applyChange(Event change) throws EventApplyException {
		applyChange(change, true);
	}

	private void applyChange(Event event, boolean isNew) throws EventApplyException {
		Method method = null;
		try {
			method = this.getClass().getDeclaredMethod("apply", event.getClass());
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}

		if (method != null) {
			method.setAccessible(true);
			try {
				method.invoke(this, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new EventApplyException(e.getMessage(), e);
			}
		}

		if (isNew) {
			changeEvents.add(event);
		}
	}
}
