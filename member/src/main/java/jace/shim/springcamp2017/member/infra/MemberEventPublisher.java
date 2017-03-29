package jace.shim.springcamp2017.member.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class MemberEventPublisher implements EventPublisher<MemberRawEvent> {

	@Value("${kafka.member.topic}")
	String memberKafkaTopic;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Async
	@Override
	public void publish(MemberRawEvent rawEvent) {
		if (rawEvent == null) {
			return;
		}

		try {
			final String sendMessage = objectMapper.writeValueAsString(rawEvent);
			kafkaTemplate.send(memberKafkaTopic, sendMessage);
			log.debug("{} 전송 완료  - {}", this.memberKafkaTopic, sendMessage);
		} catch (final JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
}
