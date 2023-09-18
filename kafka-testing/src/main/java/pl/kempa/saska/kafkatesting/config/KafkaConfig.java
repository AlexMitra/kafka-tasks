package pl.kempa.saska.kafkatesting.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

	@Value("${test.topic-1.name}")
	private String topic;

	@Value("${test.topic-1.partition}")
	private int partitionNumber;

	@Value("${test.topic-1.replication}")
	private short replicationFactor;

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(topic)
				.partitions(partitionNumber)
				.replicas(replicationFactor)
				.compact()
				.build();
	}
}
