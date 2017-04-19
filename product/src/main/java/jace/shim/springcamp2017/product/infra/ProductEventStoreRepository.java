package jace.shim.springcamp2017.product.infra;

import jace.shim.springcamp2017.product.model.event.ProductRawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
@Slf4j
public class ProductEventStoreRepository {

	private static final String TABLE_NAME = "raw_event";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long save(ProductRawEvent commonRawEvent) {
		StringBuilder query = new StringBuilder();
		query.append(format("INSERT INTO %s ", TABLE_NAME));
		query.append(" ( identifier, type, version, payload, created ) ");
		query.append(" VALUES ( ?, ?, ?, ?, ? ) ");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			int idx = 0;
			statement.setLong(++idx, commonRawEvent.getIdentifier());
			statement.setString(++idx, commonRawEvent.getType());
			statement.setLong(++idx, commonRawEvent.getVersion());
			statement.setString(++idx, commonRawEvent.getPayload());
			statement.setTimestamp(++idx, convertLocalDateTimeToTimestamp(commonRawEvent.getCreated()));

			return statement;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}

	public List<ProductRawEvent> findByIdentifier(Long identifier) {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());
		query.append(" WHERE identifier = ?");

		log.debug("-> find query : " + query.toString());

		return jdbcTemplate.query(query.toString(), new Object[]{identifier}, new CommonRawEventRowMapper());
	}

	public List<ProductRawEvent> findAll() {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());

		return jdbcTemplate.query(query.toString(), new CommonRawEventRowMapper());
	}

	public List<ProductRawEvent> findByIdentifierAndVersionGreaterThan(Long identifier, Long version) {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());
		query.append(" WHERE identifier = ?");
		query.append("   AND version >= ?");

		return jdbcTemplate.query(query.toString(), new Object[]{identifier, version}, new CommonRawEventRowMapper());
	}

	private static String selectQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT seq, identifier, type, version, payload, created ");
		query.append(format(" FROM %s ", TABLE_NAME));

		return query.toString();
	}

	/**
	 * 상품 unique id 생성
	 * @return
	 */
	public synchronized Long createProductId() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE sequence SET productId=LAST_INSERT_ID(productId+1)");

		jdbcTemplate.update(query.toString());

		query.setLength(0);

		query.append(" SELECT LAST_INSERT_ID() ");

		return jdbcTemplate.queryForObject(query.toString(), Long.class);
	}

	private static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}

	static class CommonRawEventRowMapper implements RowMapper<ProductRawEvent> {

		@Override
		public ProductRawEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
			final ProductRawEvent commonRawEvent = new ProductRawEvent();
			commonRawEvent.setSeq(rs.getLong("seq"));
			commonRawEvent.setIdentifier(rs.getLong("identifier"));
			commonRawEvent.setType(rs.getString("type"));
			commonRawEvent.setVersion(rs.getLong("version"));
			commonRawEvent.setPayload(rs.getString("payload"));

			final Timestamp created = rs.getTimestamp("created");

			commonRawEvent.setCreated(convertTimestampToLocalDateTime(created));

			return commonRawEvent;
		}

		private static LocalDateTime convertTimestampToLocalDateTime(Timestamp dateTime) {
			return dateTime.toLocalDateTime();
		}

	}
}
