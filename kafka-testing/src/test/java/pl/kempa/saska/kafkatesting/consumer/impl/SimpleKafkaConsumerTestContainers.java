package pl.kempa.saska.kafkatesting.consumer.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import pl.kempa.saska.kafkatesting.KafkaTestingApp;
import pl.kempa.saska.kafkatesting.consumer.KafkaConsumer;
import pl.kempa.saska.kafkatesting.producer.KafkaProducer;

@SpringBootTest(classes = KafkaTestingApp.class)
@DirtiesContext
@Testcontainers
class SimpleKafkaConsumerTestContainers {

	@Container
	public static KafkaContainer kafkaContainer =
			new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
	}

	@Autowired private KafkaProducer producer;
	@SpyBean private KafkaConsumer consumer;
	@Captor private ArgumentCaptor<String> payloadCaptor;

	@Captor private ArgumentCaptor<String> topicCaptor;

	@Value("${test.topic-1.name}")
	private String topic;

	@Test
	void onConsume_producerSendsMessage_consumerReceivesMessage() {
		// given
		String template = "Simple message for kafka testing ";
		List<String> messages =
				IntStream.range(0, 10)
						.mapToObj(i -> template + i)
						.toList();

		// when
		messages.forEach(m -> producer.produce(topic, m));

		// then
		verify(consumer, timeout(1000).times(messages.size())).onConsume(payloadCaptor.capture(),
				topicCaptor.capture());
		List<String> batchPayload = payloadCaptor.getAllValues();
		assertNotNull(batchPayload);
		assertThat(batchPayload.size(), equalTo(messages.size()));
		assertThat(topicCaptor.getValue(), equalTo(topic));
		assertThat(batchPayload, everyItem(stringContainsInOrder(template)));
	}
}