package pl.kempa.saska.trackerservice.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pl.kempa.saska.trackerservice.dto.DistanceDTO;
import pl.kempa.saska.trackerservice.dto.VehicleDTO;

@Configuration
@EnableTransactionManagement
public class KafkaConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value(value = "${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${app.coordinates-logging-topic.name}")
	private String loggingTopic;

	@Value("${app.coordinates-logging-topic.partition}")
	private int partitionNumber;

	@Value("${app.coordinates-logging-topic.replication}")
	private short replicationFactor;

	@Bean
	public ConsumerFactory<String, VehicleDTO> consumerFactory() {
		var props = Map.of(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress,
				ConsumerConfig.GROUP_ID_CONFIG,
				groupId,
				ConsumerConfig.ISOLATION_LEVEL_CONFIG,
				IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));
		return new DefaultKafkaConsumerFactory(props, new StringDeserializer(),
				new JsonDeserializer(VehicleDTO.class, false));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, VehicleDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, VehicleDTO> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties()
				.setAckMode(ContainerProperties.AckMode.RECORD);
		return factory;
	}

	@Bean
	public ProducerFactory<String, DistanceDTO> producerFactory() {
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
		configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "logging-tx");
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, DistanceDTO> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public KafkaTransactionManager kafkaTransactionManager(final ProducerFactory producerFactoryTransactional) {
		return new KafkaTransactionManager<>(producerFactoryTransactional);
	}


	@Bean
	public NewTopic loggingTopic() {
		return TopicBuilder.name(loggingTopic)
				.partitions(partitionNumber)
				.replicas(replicationFactor)
				.compact()
				.build();
	}
}
