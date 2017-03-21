package jace.shim.springcamp2017.product.service;

import jace.shim.springcamp2017.core.exception.EventApplyException;
import jace.shim.springcamp2017.product.infra.ProductEventHandler;
import jace.shim.springcamp2017.product.model.Product;
import jace.shim.springcamp2017.product.model.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductEventHandler eventRepository;

	/**
	 * 상품 등록
	 *
	 * @param productCreateCommand
	 * @return
	 */
	public Long add(ProductCreateCommand productCreateCommand) throws EventApplyException {
		Product product = Product.create(productCreateCommand);
		// 이벤트 저장
		eventRepository.save(product);

		return product.getIdentifier();
	}

	/**
	 * 상품명 변경
	 * @param productChangeNameCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changeName(ProductChangeNameCommand productChangeNameCommand) throws EventApplyException {
		final Product product = eventRepository.find(productChangeNameCommand.getProductId());
		product.changeName(productChangeNameCommand);

		eventRepository.save(product);

		return product;
	}

	/**
	 * 상품 가격 변경
	 * @param productChangePriceCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changePrice(ProductChangePriceCommand productChangePriceCommand) throws EventApplyException {
		final Product product = eventRepository.find(productChangePriceCommand.getProductId());
		product.changePrice(productChangePriceCommand);

		eventRepository.save(product);

		return product;
	}

	/**
	 * 상품 조회
	 *
	 * @param productId
	 * @return
	 */
	public Product getProduct(Long productId) throws EventApplyException {
		return eventRepository.find(productId);
	}

	/**
	 * 상품 수량 증가 처리
	 * @param productIncreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product increaseQuantity(ProductIncreaseQuantityCommand productIncreaseQuantityCommand) throws EventApplyException {
		final Product product = eventRepository.find(productIncreaseQuantityCommand.getProductId());
		product.increaseQuantity(productIncreaseQuantityCommand);

		eventRepository.save(product);

		return product;
	}

	/**
	 * 상품 수량 감소 처리
	 * @param productDecreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product decreaseQuantity(ProductDecreaseQuantityCommand productDecreaseQuantityCommand) throws EventApplyException {
		final Product product = eventRepository.find(productDecreaseQuantityCommand.getProductId());
		product.decreaseQuantity(productDecreaseQuantityCommand);

		eventRepository.save(product);

		return product;
	}
}
