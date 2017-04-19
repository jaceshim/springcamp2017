package jace.shim.springcamp2017.order.infra.remote;

import jace.shim.springcamp2017.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Component
public class ProductInfoRepository {

	private static final String PRODUCT_SERVICE_HOST = "product.jaceshim.com";
	private static final int PRODUCT_SERVICE_PORT = 30001;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 상품조회 by product api
	 * @param productId
	 * @return
	 */
	public Product findByProductId(Long productId) {
		URI uri = UriComponentsBuilder.newInstance().scheme("http").host(PRODUCT_SERVICE_HOST).port(PRODUCT_SERVICE_PORT)
			.path("/api/products/" + productId).build()
			.encode()
			.toUri();

		Product result = restTemplate.getForObject(uri,
			Product.class);

		return new Product(result.getProductId(), result.getName(), result.getPrice(), result.getInventory());
	}

}
