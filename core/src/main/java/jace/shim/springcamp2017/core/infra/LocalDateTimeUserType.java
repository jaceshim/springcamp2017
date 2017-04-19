package jace.shim.springcamp2017.core.infra;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * <p>LocalDateTime Hibernate User Type.</p>
 * <p>
 * <em>Beware LocalDateTime/Instant support nanoseconds, but java.util.Date only supports milliseconds.</em>
 * <em><code>LocaDateTimeUserType</code> changes <code>LocalDateTime</code> to <code>java.util.Date</code> internally,
 * so nanoseconds part will be truncated.</em>
 * </p>
 *
 * @see LocalDateTime
 * @see Date
 * @see Date#from(Instant)
 * @see Instant
 */
public class LocalDateTimeUserType implements EnhancedUserType {
	private static final int SQL_TYPE = Types.TIMESTAMP;

	@Override
	public int[] sqlTypes() {
		return new int[] { SQL_TYPE };
	}

	@Override
	public Class returnedClass() {
		return LocalDateTime.class;
	}

	@Override
	@SuppressWarnings("PMD.SuspiciousEqualsMethodName") // Hibernate spec상 어쩔수 없음.
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return Objects.hashCode(x);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		Date timestamp = (Date) StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names, session, owner);

		if (timestamp == null) {
			return null;
		}

		return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index, session);
			return;
		}

		LocalDateTime localDateTime = (LocalDateTime) value;
		final Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date timestamp = Date.from(instant);
		StandardBasicTypes.TIMESTAMP.nullSafeSet(st, timestamp, index, session);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		// no need to deepCopy immutable objects
		return value;
	}

	@Override
	public boolean isMutable() {
		// LocalDateTime is immutable
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		// no need to replace immutable objects
		return original;
	}

	@Override
	public String objectToSQLString(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toXMLString(Object value) {
		return Objects.toString(value);
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		return LocalDateTime.parse(xmlValue);
	}
}
