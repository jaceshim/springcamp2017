package jace.shim.springcamp2017.product.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Component
@Slf4j
public class ProductEventPublisher implements EventPublisher {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Value("${kafka.product.topic}")
	private String productKafkaTopic;

	@Async
	@Override
	public void publish(Event event) {
		if (event == null) {
			return;
		}

		try {
			final String sendMessage = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(productKafkaTopic, sendMessage);
			log.debug("{} 전송 완료  - {}", this.productKafkaTopic, sendMessage);
		} catch (final JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
}
