package jace.shim.springcamp2017.member.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@Slf4j
public class KafkaListener {

	private final CountDownLatch latch1 = new CountDownLatch(1);

	private ObjectMapper objectMapper;

	public KafkaListener() {
	}

//	@org.springframework.kafka.annotation.KafkaListener(id = "member-consumer-group", topics = "member-event-topic")
//	public void memberEventListener(String message) throws ClassNotFoundException, IOException {
//
//		log.debug("receive message : {}", message);
//
//		MemberRawEvent rawEvent = objectMapper.readValue(message, MemberRawEvent.class);
//		final Class<?> eventType = Class.forName(rawEvent.getType());
//		final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);
//
//		this.latch1.countDown();
//	}

}
