package jace.shim.springcamp2017.order.service;

import jace.shim.springcamp2017.member.model.read.Member;
import jace.shim.springcamp2017.order.infra.OrderEventHandler;
import jace.shim.springcamp2017.order.infra.OrderEventStoreRepository;
import jace.shim.springcamp2017.order.infra.remote.MemberInfoRepository;
import jace.shim.springcamp2017.order.infra.remote.ProductInfoRepository;
import jace.shim.springcamp2017.order.model.Delivery;
import jace.shim.springcamp2017.order.model.Order;
import jace.shim.springcamp2017.order.model.OrderItem;
import jace.shim.springcamp2017.order.model.command.OrderCommand;
import jace.shim.springcamp2017.product.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 4. 6..
 */
@Service
@Slf4j
public class OrderService {
	@Autowired
	private OrderEventHandler orderEventHandler;

	@Autowired
	private OrderEventStoreRepository orderEventStoreRepository;

	@Autowired
	private MemberInfoRepository memberInfoRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	/**
	 * 주문
	 *
	 * @param orderCreateCommand
	 * @return
	 */
	public Order createProduct(OrderCommand.CreateOrder orderCreateCommand) {
		// DB sequence를 통해서 유일한 orderId값 생성
		final Long orderId = orderEventStoreRepository.createOrderId();
		final Delivery delivery = new Delivery(orderCreateCommand.getDelivery().getAddress(),
			orderCreateCommand.getDelivery().getPhone(),
			orderCreateCommand.getDelivery().getDeliveryMessage());

		final List<OrderItem> orderItems = orderCreateCommand.getOrderItems().stream().map(checkoutItem -> {
			final Product readProduct = productInfoRepository.findByProductId(checkoutItem.getProductId());

			final jace.shim.springcamp2017.product.model.Product modelProduct = new jace.shim.springcamp2017.product.model.Product(
				readProduct.getProductId(),
				readProduct.getName(), readProduct.getPrice(), readProduct.getInventory());

			return new OrderItem(modelProduct, checkoutItem.getQuantity());
		}).collect(Collectors.toList());

		final Member readMember = memberInfoRepository.findById(orderCreateCommand.getMemberId());

		final jace.shim.springcamp2017.member.model.Member modelMember = new jace.shim.springcamp2017.member.model.Member(readMember.getId(),
			readMember.getName(), readMember.getEmail(), readMember.getAddress());

		final Order newOrder = Order.order(orderId, modelMember, delivery, orderItems);

		orderEventHandler.save(newOrder);

		return newOrder;
	}
}
