package jace.shim.springcamp2017.order.infra.read;

import jace.shim.springcamp2017.order.model.read.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Repository
public interface ProductReadRepository extends JpaRepository<Product, Long> {
}
