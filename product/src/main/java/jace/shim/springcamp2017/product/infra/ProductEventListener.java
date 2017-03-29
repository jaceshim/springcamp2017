package jace.shim.springcamp2017.product.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Component
@Slf4j
public class ProductEventListener extends AbstractEventListener {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

}
