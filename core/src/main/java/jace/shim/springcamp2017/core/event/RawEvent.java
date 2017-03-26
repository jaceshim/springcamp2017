package jace.shim.springcamp2017.core.event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public interface RawEvent<ID> {

	ID getIdentifer();

	String getType();

	Long getVersion();

	String getPayload();
}
