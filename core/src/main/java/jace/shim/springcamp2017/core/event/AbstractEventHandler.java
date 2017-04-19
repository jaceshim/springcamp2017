package jace.shim.springcamp2017.core.event;

import jace.shim.springcamp2017.core.domain.AggregateRoot;
import jace.shim.springcamp2017.core.snapshot.Snapshot;
import jace.shim.springcamp2017.core.snapshot.SnapshotRepository;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by jaceshim on 2017. 3. 5..
 */
@Slf4j
public abstract class AbstractEventHandler<A extends AggregateRoot, ID> implements EventHandler<A, ID> {

	private final Class<A> aggregateType;

	private EventStore<ID> eventStore;

	private SnapshotRepository<A, ID> snapshotRepository;

	private Map<ID, AtomicInteger> snapshotCountMap = new ConcurrentHashMap<>();

	private static final int SNAPSHOT_COUNT = 10;

	public AbstractEventHandler(EventStore eventStore, SnapshotRepository snapshotRepository) {
		this.eventStore = eventStore;
		this.snapshotRepository = snapshotRepository;
		this.aggregateType = aggregateType();
	}

	private Class<A> aggregateType() {
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
		try {
			Constructor[] constructors = aggregateType.getDeclaredConstructors();
			for (Constructor constructor : constructors) {
				if (constructor.getParameterCount() == 1) {
					constructor.setAccessible(true);
					return (A) constructor.newInstance(identifier);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		throw new IllegalArgumentException("Aggregate에 identifier를 argument로 받는 생성자가 없음");
	}

	@Override
	public void save(A aggregateRoot) {
		final ID identifier = (ID) aggregateRoot.getIdentifier();
		// 이벤트 저장소에 이벤트 저장
		eventStore.saveEvents(identifier, aggregateRoot.getExpectedVersion(), aggregateRoot.getUncommittedChanges());

		// 미처리된 이벤트 목록 clear
		aggregateRoot.markChangesAsCommitted();
		AtomicInteger snapshotCount = snapshotCountMap.get(identifier);
		if (snapshotCount == null) {
			snapshotCount = new AtomicInteger(0);
		}

		if (snapshotCount.get() == SNAPSHOT_COUNT) {
			log.debug("{} snapshot count {}", identifier, snapshotCount.get());
			Snapshot<A, ID> snapshot = new Snapshot<>(identifier, aggregateRoot.getExpectedVersion(), aggregateRoot);
			snapshotRepository.save(snapshot);
			snapshotCount.set(0);

			return;
		}

		final int increaseCount = snapshotCount.incrementAndGet();
		log.debug("{} snapshot increase count {}", increaseCount);
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

		if (baseEvents == null || baseEvents.size() == 0) {
			return null;
		}

		aggregateRoot.replay(baseEvents);

		return aggregateRoot;
	}

	@Override
	public List<A> findAll() {
		List<A> result = new ArrayList<>();

		final List<Event<ID>> allEventsOpt = eventStore.getAllEvents();
		if (allEventsOpt == null) {
			return result;
		}

		final Map<ID, List<Event<ID>>> eventsByIdentifier = allEventsOpt.stream().collect(groupingBy(Event::getIdentifier));
		for (Map.Entry<ID, List<Event<ID>>> entry : eventsByIdentifier.entrySet()) {
			A aggregateRoot = createAggregateRootViaReflection(entry.getKey());
			aggregateRoot.replay(entry.getValue());

			result.add(aggregateRoot);
		}

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
