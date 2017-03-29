package jace.shim.springcamp2017.core.event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public interface RawEvent<ID> {

	ID getIdentifier();

	String getType();

	Long getVersion();

	String getPayload();
}
