package jace.shim.springcamp2017.product.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jaceshim on 2017. 3. 24..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FieldErrorResource {
	private String resource;
	private String field;
	private String code;
	private String message;
}
