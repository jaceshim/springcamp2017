package jace.shim.springcamp2017.member.infra;

import jace.shim.springcamp2017.core.event.AbstractEventProjector;
import jace.shim.springcamp2017.member.model.event.*;
import jace.shim.springcamp2017.order.model.OrderItem;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
import jace.shim.springcamp2017.product.model.event.ProductCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Component
@Slf4j
public class MemberEventProjector extends AbstractEventProjector {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 상품주문 projection
	 * @param event
	 */
	public void execute(ProductCreated event) {
		// query모델은 발생한 이벤트를 선택적으로 취해서 마음데로 query전용 table을 구성하고 관리할 수 있다.
		// 즉 실제 product 도메인의 모든정보를 관리할 필요가 없고 ( 이유도 없다 ) member 도메인에서 필요한 정보만 선택해서 테이블로 관리하면 된다
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO product (");
		query.append(" productId, name, price, imagePath, description ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getProductId(),
			event.getName(),
			event.getPrice(),
			event.getImagePath(),
			event.getDescription());
	}

	/**
	 * 상품주문 projection
	 * @param event
	 */
	public void execute(OrderCreated event) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO `order` (");
		query.append(" orderId, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			convertLocalDateTimeToTimestamp(event.getCreated()));

		query.setLength(0);

		query.append("INSERT INTO order_member (");
		query.append(" orderId, memberId ");
		query.append(") VALUES ( ");
		query.append(" ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			event.getOrderMember().getId());

		query.setLength(0);

		query.append("INSERT INTO order_delivery (");
		query.append(" orderId, address, phone, deliveryMessage ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?) ");

		jdbcTemplate.update(query.toString(),
			event.getOrderId(),
			event.getDelivery().getAddress(),
			event.getDelivery().getPhone(),
			event.getDelivery().getDeliveryMessage());

		query.setLength(0);

		query.append("INSERT INTO order_items (");
		query.append(" orderId, productId, price, quantity ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?) ");

		final Set<OrderItem> orderItems = event.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			jdbcTemplate.update(query.toString(),
				event.getOrderId(),
				orderItem.getProduct().getProductId(),
				orderItem.getProduct().getPrice(),
				orderItem.getQuantity());
		}
	}

	/**
	 * 회원 등록 projection
	 * @param event
	 */
	public void execute(MemberCreated event) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO member (");
		query.append(" id, name, email, password, address, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?, ?, ? ) ");

		jdbcTemplate.update(query.toString(),
			event.getId(),
			event.getName(),
			event.getEmail(),
			event.getPassword(),
			event.getAddress(),
			convertLocalDateTimeToTimestamp(event.getCreated()));
	}

	/**
	 * 회원정보 변경 projection
	 *
	 * @param event
	 */
	public void execute(MemberProfileChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET name = ? ");
		query.append("	 , email = ? ");
		query.append("	 , address = ? ");
		query.append("   , updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getName(), event.getEmail(), event.getAddress(),
			convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 비밀번호 변경 projection
	 *
	 * @param event
	 */
	public void execute(MemberPasswordChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET password = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getNewPassword(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 탈퇴여부 변경 projection
	 *
	 * @param event
	 */
	public void execute(MemberWithdrawalChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET withdrawal = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.isWithdrawal(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 로그인 projection
	 *
	 * @param event
	 */
	public void execute(MemberLogined event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET logined = ? ");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), convertLocalDateTimeToTimestamp(event.getLogined()), event.getId());
	}

}
