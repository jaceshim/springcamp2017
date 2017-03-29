package jace.shim.springcamp2017.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"identifier", "expectedVersion", "uncommittedChanges"})
public abstract class AggregateRoot<ID> implements Serializable {

	private static final String APPLY_METHOD_NAME = "apply";

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

	public void replay(List<Event> changes) {
		for (Event change : changes) {
			applyChange(change, false);
			this.expectedVersion++;
		}
	}

	protected void applyChange(Event change) {
		applyChange(change, true);
	}

	private void applyChange(Event event, boolean isNew) {
		Method method;
		try {
			method = this.getClass().getDeclaredMethod(APPLY_METHOD_NAME, event.getClass());
			if (method != null) {
				method.setAccessible(true);
				method.invoke(this, event);
			}

			if (isNew) {
				changeEvents.add(event);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			log.error(e.getMessage(), e);
			throw new EventApplyException(e.getMessage(), e);
		}
	}
}
