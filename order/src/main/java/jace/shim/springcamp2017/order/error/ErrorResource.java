package jace.shim.springcamp2017.order.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 24..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class ErrorResource {
	private String code;
	private String message;
	private List<FieldErrorResource> fieldErrors;

	public ErrorResource(String code, String message) {
		this.code = code;
		this.message = message;
	}
}