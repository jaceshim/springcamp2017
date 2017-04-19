package jace.shim.springcamp2017.member.infra;

import jace.shim.springcamp2017.core.event.AbstractEventProjector;
import jace.shim.springcamp2017.member.model.event.*;
import jace.shim.springcamp2017.order.model.OrderItem;
import jace.shim.springcamp2017.order.model.event.OrderCreated;
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
