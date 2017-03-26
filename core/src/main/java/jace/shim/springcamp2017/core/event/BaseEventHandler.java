package jace.shim.springcamp2017.core.event;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.core.snapshot.Snapshot;
import jace.shim.springcamp2017.core.snapshot.SnapshotRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by jaceshim on 2017. 3. 5..
 */
public abstract class BaseEventHandler<A extends AggregateRoot, ID> implements EventHandler<A, ID> {

	private final Class aggregateType;

	private EventStore<ID> eventStore;

	private SnapshotRepository<A, ID> snapshotRepository;

	public BaseEventHandler(EventStore eventStore, SnapshotRepository snapshotRepository) {
		this.eventStore = eventStore;
		this.snapshotRepository = snapshotRepository;
		this.aggregateType = aggregateType();
	}

	private Class aggregateType() {
		Type genType = this.getClass().getGenericSuperclass();
		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

			if ((params != null) && (params.length >= 1)) {
				return (Class) params[0];
			}
		}
		return null;
	}

	private A createAggregateRootViaReflection(ID identifier) {
		Constructor[] cons = aggregateType.getDeclaredConstructors();
		cons[0].setAccessible(true);
		try {
			return (A) cons[0].newInstance(identifier);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void save(A aggregateRoot) {
		final ID identifier = (ID) aggregateRoot.getIdentifier();
		// 이벤트 저장소에 이벤트 저장
		eventStore.saveEvents(identifier, aggregateRoot.getExpectedVersion(), aggregateRoot.getUncommittedChanges());
		// 미처리된 이벤트 목록 clear
		aggregateRoot.markChangesAsCommitted();
		// TODO: 2017. 3. 9.snapshot을 어떤 기준으로 생성할까?? ( count or time )
	}

	@Override
	public A find(ID identifier) {
		// snapshot저장소에서 호출함
		A aggregateRoot = createAggregateRootViaReflection(identifier);

		Optional<Snapshot<A, ID>> retrieveSnapshot = retrieveSnapshot(identifier);
		List<Event<ID>> baseEvents;
		if (retrieveSnapshot.isPresent()) {
			Snapshot<A, ID> snapshot = retrieveSnapshot.get();
			baseEvents = eventStore.getEventsByAfterVersion(snapshot.getIdentifier(), snapshot.getVersion());
			// snapshot에 저장된 aggregateRoot객체를 로딩함.
			aggregateRoot = snapshot.getAggregateRoot();
		} else {
			baseEvents = eventStore.getEvents(identifier);
		}

		aggregateRoot.replay(baseEvents);

		return aggregateRoot;
	}

	@Override
	public List<A> findAll() {
		List<A> result = new ArrayList<>();

		final Optional<List<Event<ID>>> allEventsOpt = Optional.ofNullable(eventStore.getAllEvents());
		allEventsOpt.ifPresent(allEvents -> {
			final Map<ID, List<Event<ID>>> eventsByIdentifier = allEvents.stream().collect(groupingBy(Event::getIdentifier));
			for (Map.Entry<ID, List<Event<ID>>> entry : eventsByIdentifier.entrySet()) {
				A aggregateRoot = createAggregateRootViaReflection(entry.getKey());
				aggregateRoot.replay(entry.getValue());

				result.add(aggregateRoot);
			}
		});

		return result;
	}

	/**
	 * Get the snapshot
	 * @param identifier
	 * @return
	 */
	private Optional<Snapshot<A, ID>> retrieveSnapshot(ID identifier) {
		if (snapshotRepository == null) {
			return Optional.empty();
		}
		return snapshotRepository.findLatest(identifier);
	}

}
