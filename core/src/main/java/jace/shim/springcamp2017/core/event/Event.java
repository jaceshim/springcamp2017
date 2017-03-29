package jace.shim.springcamp2017.core.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by jaceshim on 2017. 3. 7..
 */
public interface Event<ID> extends Serializable {
	@JsonIgnore
	ID getIdentifier();
}
