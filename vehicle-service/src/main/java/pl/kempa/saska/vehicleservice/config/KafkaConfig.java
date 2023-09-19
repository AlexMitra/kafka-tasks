package pl.kempa.saska.vehicleservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import pl.kempa.saska.vehicleservice.dto.VehicleDTO;

@Configuration
public class KafkaConfig {

	@Value("${app.vehicle-coordinates-topic.name}")
	private String topic;

	@Value("${app.vehicle-coordinates-topic.partition}")
	private int partitionNumber;

	@Value("${app.vehicle-coordinates-topic.replication}")
	private short replicationFactor;

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public ProducerFactory<String, VehicleDTO> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				JsonSerializer.class);
		configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, VehiclePartitioner.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, VehicleDTO> greetingKafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}


	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(topic)
				.partitions(partitionNumber)
				.replicas(replicationFactor)
				.compact()
				.build();
	}
}
