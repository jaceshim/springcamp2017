package jace.shim.springcamp2017.member.config.kafka;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@Slf4j
public class KafkaListener {

	private final CountDownLatch latch1 = new CountDownLatch(1);

//	@org.springframework.kafka.annotation.KafkaListener(id = "product-consumer-group", topics = "product-event-topic")
//	public void listen1(String message) {
//
//		log.debug("receive message : {}", message);
//
//		this.latch1.countDown();
//	}

}
