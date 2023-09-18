package pl.kempa.saska.trackerservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.trackerservice.dto.VehicleDTO;

@Component
@Slf4j
public class VehicleCoordinatesListener2 {

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${app.vehicle-coordinates-topic.name}",
			partitions = {"1"}), groupId = "${spring.kafka.consumer.group-id}")
	public void onConsume(VehicleDTO vehicleDTO, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
		log.info("[TRACKER-2] Received vehicle with coordinates for tracking '{}' from partition {}",
				vehicleDTO, partition);
	}
}
