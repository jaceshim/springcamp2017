package jace.shim.springcamp2017.member.infra.read;

import jace.shim.springcamp2017.order.model.read.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Component
public class OrderReadRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Order> findByMemberId(String memberId) {

		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO `order` (");
		query.append(" orderId, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?) ");


		//jdbcTemplate.query();

		return null;
	}

}
