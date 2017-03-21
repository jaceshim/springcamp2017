package jace.shim.springcamp2017.product.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.core.event.Event;
import jace.shim.springcamp2017.core.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Component
@Slf4j
public class ProductEventPublisher implements EventPublisher {

	@Value("${product.kafka.topic}")
	String productKafkaTopic;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaProducer<String, String> productEventProducer;

	@Async
	@Override
	public void publish(Event event) {
		if (event == null) {
			return;
		}

		try {
			final String sendMessage = objectMapper.writeValueAsString(event);
			final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(this.productKafkaTopic, sendMessage);
			productEventProducer.send(producerRecord);
			log.debug("{} 전송 완료  - {}", this.productKafkaTopic, sendMessage);

			productEventProducer.send(producerRecord);
		} catch (final JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
}
