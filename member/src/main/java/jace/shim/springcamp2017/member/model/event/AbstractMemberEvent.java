package jace.shim.springcamp2017.member.model.event;

import jace.shim.springcamp2017.core.event.Event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public abstract class AbstractMemberEvent implements Event<String> {
	private String id;

	public AbstractMemberEvent(String id) {
		this.id = id;
	}

	@Override
	public String getIdentifier() {
		return this.id;
	}
}
