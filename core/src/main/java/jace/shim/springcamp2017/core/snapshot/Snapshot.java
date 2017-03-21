package jace.shim.springcamp2017.core.snapshot;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created by jaceshim on 2017. 3. 7..
 */
@Getter
public class Snapshot<A extends AggregateRoot, ID> implements Serializable {

	private ID identifier;

	private Long version;

	private A aggregateRoot;

	public Snapshot(ID identifier, Long version, A aggregateRoot) {
		this.identifier = identifier;
		this.version = version;
		this.aggregateRoot = aggregateRoot;
	}
}
