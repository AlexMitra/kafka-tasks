package pl.kempa.saska.kafkatesting.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaProducer {

	private KafkaTemplate<String, String> kafkaTemplate;

	public void produce(String topic, String message) {
		log.info("sending message '{}' to topic '{}'", message, topic);
		kafkaTemplate.send(topic, message);
	}
}
