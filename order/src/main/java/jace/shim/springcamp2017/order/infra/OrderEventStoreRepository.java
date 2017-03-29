package jace.shim.springcamp2017.order.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public interface OrderEventStoreRepository extends JpaRepository<OrderRawEvent, Long> {

	List<OrderRawEvent> findByIdentifier(Long identifier);

	List<OrderRawEvent> findByIdentifierAndVersionGreaterThan(Long identifier, Long version);
}
