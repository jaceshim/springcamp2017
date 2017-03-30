package jace.shim.springcamp2017.product.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public class ProductEventStoreRepository {

	private static final String TABLE_NAME = "raw_event";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long save(ProductRawEvent productRawEvent) {
		StringBuilder query = new StringBuilder();
		query.append(format("INSERT INTO %s ", TABLE_NAME));
		query.append(" ( identifier, type, version, payload, created ) ");
		query.append(" VALUES ( ?, ?, ?, ?, ? ) ");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			int idx = 0;
			statement.setLong(++idx, productRawEvent.getIdentifier());
			statement.setString(++idx, productRawEvent.getType());
			statement.setLong(++idx, productRawEvent.getVersion());
			statement.setString(++idx, productRawEvent.getPayload());
			statement.setTimestamp(++idx, convertLocalDateTimeToTimestamp(productRawEvent.getCreated()));

			return statement;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}

	public List<ProductRawEvent> findByIdentifier(Long identifier) {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());
		query.append(" WHERE identifier = ?");

		return jdbcTemplate.queryForList(query.toString(), ProductRawEvent.class, identifier);
	}

	public List<ProductRawEvent> findAll() {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());

		return jdbcTemplate.queryForList(query.toString(), ProductRawEvent.class);
	}

	public List<ProductRawEvent> findByIdentifierAndVersionGreaterThan(Long identifier, Long version) {
		StringBuilder query = new StringBuilder();
		query.append(selectQuery());
		query.append(" WHERE identifier = ?");
		query.append("   AND version >= ?");

		return jdbcTemplate.queryForList(query.toString(), ProductRawEvent.class, identifier, version);
	}

	private static String selectQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT seq, identifier, type, version, payload, created ");
		query.append(format(" FROM %s ", TABLE_NAME));

		return query.toString();
	}

	private Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
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
}
