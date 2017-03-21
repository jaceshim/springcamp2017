package jace.shim.springcamp2017.product.infra;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.core.snapshot.Snapshot;
import jace.shim.springcamp2017.core.snapshot.SnapshotRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jaceshim on 2017. 3. 9..
 */
@Repository
public class ProductSnapshotRepository<A extends AggregateRoot, ID> implements SnapshotRepository<A, ID> {

	private final List<Snapshot<A, ID>> snapshots = new CopyOnWriteArrayList<>();

	@Override
	public Optional<Snapshot<A, ID>>findLatest(ID identifier) {
		return snapshots.stream()
			.filter(snapshot -> snapshot.getIdentifier().equals(identifier))
			.reduce((s1, s2) -> s1.getVersion() > s2.getVersion() ? s1 : s2);
	}

	@Override
	public void save(Snapshot<A, ID> snapshot) {
		snapshots.add(snapshot);
	}
}
