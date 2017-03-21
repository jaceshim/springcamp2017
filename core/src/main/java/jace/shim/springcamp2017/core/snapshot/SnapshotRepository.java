package jace.shim.springcamp2017.core.snapshot;

import jace.shim.springcamp2017.core.domain.AggregateRoot;

import java.util.Optional;

/**
 * Created by jaceshim on 2017. 3. 9..
 */
public interface SnapshotRepository<A extends AggregateRoot, ID> {

	Optional<Snapshot<A, ID>> findLatest(ID id);

	void save(Snapshot<A, ID> snapshot);
}
