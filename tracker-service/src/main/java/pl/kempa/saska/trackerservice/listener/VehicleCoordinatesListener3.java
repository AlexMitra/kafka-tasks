package pl.kempa.saska.trackerservice.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.trackerservice.dto.VehicleDTO;
import pl.kempa.saska.trackerservice.service.LoggingService;

@Component
@Slf4j
public class VehicleCoordinatesListener3 {

	@Value("${app.coordinates-logging-topic.name}")
	private String loggingTopic;
	@Autowired private LoggingService loggingService;

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${app.vehicle-coordinates-topic.name}",
			partitions = {"2"}), groupId = "${spring.kafka.consumer.group-id}")
	public void onConsume(VehicleDTO vehicleDTO, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
		log.info("[TRACKER-3] Received vehicle with coordinates for tracking '{}' from partition {}",
				vehicleDTO, partition);
		loggingService.sendMessage(loggingTopic, vehicleDTO);
	}
}
