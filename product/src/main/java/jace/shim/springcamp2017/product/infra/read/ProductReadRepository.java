package jace.shim.springcamp2017.product.infra.read;

import jace.shim.springcamp2017.product.model.read.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Repository
public interface ProductReadRepository extends JpaRepository<Product, Long> {

	Product findByProductId(Long productId);
}
