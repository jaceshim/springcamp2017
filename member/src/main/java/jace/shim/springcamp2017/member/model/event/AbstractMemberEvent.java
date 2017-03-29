package jace.shim.springcamp2017.member.model.event;

import jace.shim.springcamp2017.core.event.Event;
import lombok.Getter;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public abstract class AbstractMemberEvent implements Event<String> {
	protected String id;

	@Override
	public String getIdentifier() {
		return this.id;
	}
}
