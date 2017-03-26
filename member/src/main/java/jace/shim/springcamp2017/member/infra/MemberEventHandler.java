package jace.shim.springcamp2017.member.infra;

import jace.shim.springcamp2017.core.event.BaseEventHandler;
import jace.shim.springcamp2017.core.event.EventStore;
import jace.shim.springcamp2017.core.snapshot.SnapshotRepository;
import jace.shim.springcamp2017.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
public class MemberEventHandler extends BaseEventHandler<Member, String> {

	@Autowired
	public MemberEventHandler(EventStore eventStore,
		SnapshotRepository snapshotRepository) {
		super(eventStore, snapshotRepository);
	}
}
