package jace.shim.springcamp2017.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public interface ProductEventStoreRepository extends JpaRepository<ProductRawEvent, Long> {

	List<ProductRawEvent> findByIdentifer(Long identifier);

	List<ProductRawEvent> findByIdentiferAndVersionGreaterThan(Long identifier, Long version);
}
