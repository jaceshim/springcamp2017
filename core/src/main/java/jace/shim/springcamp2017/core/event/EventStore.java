package jace.shim.springcamp2017.core.event;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 5..
 */
public interface EventStore<ID> {

	/**
	 * 이벤트를 저장한다
	 *
	 * @param identifier
	 * @param expectedVersion
	 * @param baseEvents
	 */
	void saveEvents(ID identifier, Long expectedVersion, List<Event> baseEvents);

	/**
	 * 주어진 identifier 의 저장된 이벤트를 얻는다
	 * @param identifier
	 * @return
	 */
	List<Event> getEvents(ID identifier);

	/**
	 * 주어진 identifier의 저장된 이벤트중 주어진 version이후 이벤트를 얻는다
	 * @param identifier
	 * @param version
	 * @return
	 */
	List<Event> getEventsByAfterVersion(ID identifier, Long version);


}
