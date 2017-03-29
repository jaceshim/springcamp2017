package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.member.model.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by jaceshim on 2017. 3. 27..
 */
@Component
@Slf4j
public class MemberEventListener extends AbstractEventListener {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	//@org.springframework.kafka.annotation.KafkaListener(id = "member-consumer-group", topics = "member-event-topic")
	public void memberEventListener(String message) throws IOException, ClassNotFoundException {
		log.debug("receive message : {}", message);

		MemberRawEvent rawEvent = objectMapper.readValue(message, MemberRawEvent.class);
		final Class<?> eventType = Class.forName(rawEvent.getType());
		final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

		this.handle(event);
	}

	/**
	 * 회원 등록 projection
	 * @param event
	 */
	public void apply(MemberCreated event) {
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
	 * 회원 명 변경 projection
	 *
	 * @param event
	 */
	public void apply(MemberNameChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET name = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getName(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 명 변경 projection
	 *
	 * @param event
	 */
	public void apply(MemberEmailChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET email = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getEmail(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 비밀번호 변경 projection
	 *
	 * @param event
	 */
	public void apply(MemberPasswordChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET password = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getNewPassword(), convertLocalDateTimeToTimestamp(event.getUpdated()),  event.getId());
	}

	/**
	 * 회원 주소 변경 projection
	 *
	 * @param event
	 */
	public void apply(MemberAddressChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET address = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.getAddress(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	/**
	 * 회원 탈퇴여부 변경 projection
	 *
	 * @param event
	 */
	public void apply(MemberWithdrawalChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE member ");
		query.append(" SET withdrawal = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE id = ?");

		jdbcTemplate.update(query.toString(), event.isWithdrawal(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getId());
	}

	private Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
