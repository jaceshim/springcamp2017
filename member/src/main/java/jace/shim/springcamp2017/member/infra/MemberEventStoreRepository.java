package jace.shim.springcamp2017.member.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public interface MemberEventStoreRepository extends JpaRepository<MemberRawEvent, String> {

	List<MemberRawEvent> findByIdentifier(String identifier);

	List<MemberRawEvent> findByIdentifierAndVersionGreaterThan(String identifier, Long version);
}
