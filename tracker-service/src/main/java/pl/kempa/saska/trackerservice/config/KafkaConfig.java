package pl.kempa.saska.trackerservice.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import pl.kempa.saska.trackerservice.dto.VehicleDTO;

@Configuration
public class KafkaConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value(value = "${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, VehicleDTO> consumerFactory() {
		var props = Map.of(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress,
				ConsumerConfig.GROUP_ID_CONFIG,
				groupId);
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
}
