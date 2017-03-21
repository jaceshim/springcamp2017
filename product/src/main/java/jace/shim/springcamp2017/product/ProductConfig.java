package jace.shim.springcamp2017.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Properties;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Configuration
@EnableAsync
@Slf4j
public class ProductConfig {
	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).modules(new JavaTimeModule()).build();
	}


	@Value("${product.kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;
	@Bean
	public KafkaProducer<String, String> productEventProducer() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", kafkaBootstrapServers);
		properties.put("acks", "all");
		properties.put("retries", 0);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

		return kafkaProducer;
	}
}
