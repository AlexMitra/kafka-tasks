package pl.kempa.saska.kafkatesting.consumer.impl;

import static java.util.Optional.ofNullable;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.kafkatesting.consumer.KafkaConsumer;

@Component
@Slf4j
public class SimpleKafkaConsumer implements KafkaConsumer<String> {

	@KafkaListener(topics = {"${test.topic-1.name}"})
	public void onConsume(String payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Received message '{}'", payload);
	}
}
