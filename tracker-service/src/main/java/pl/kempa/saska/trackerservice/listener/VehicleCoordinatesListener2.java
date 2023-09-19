package pl.kempa.saska.trackerservice.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.trackerservice.dto.DistanceDTO;
import pl.kempa.saska.trackerservice.dto.VehicleDTO;
import pl.kempa.saska.trackerservice.service.LoggingService;
import pl.kempa.saska.trackerservice.util.DistanceCalculator;

@Component
@Slf4j
public class VehicleCoordinatesListener2 {

	@Value("${app.coordinates-logging-topic.name}")
	private String loggingTopic;
	@Autowired private Map<String, VehicleDTO> cache;
	@Autowired private DistanceCalculator calculator;
	@Autowired private LoggingService loggingService;

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${app.vehicle-coordinates-topic.name}",
			partitions = {"1"}), groupId = "${spring.kafka.consumer.group-id}")
	@Transactional
	public void onConsume(VehicleDTO vehicleDTO, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
		log.info("[TRACKER-2] Received vehicle with coordinates for tracking '{}' from partition {}",
				vehicleDTO, partition);
		var prevVehicle = getVehicleFromCache(vehicleDTO.id().toString());
		if(prevVehicle != null) {
			var distance = calculator.calculateDistance(prevVehicle, vehicleDTO, "K");
			loggingService.sendMessage(loggingTopic, new DistanceDTO(vehicleDTO.id(), distance));
		}
		putToCache(vehicleDTO.id().toString(), vehicleDTO);
	}

	public void putToCache(String vehicleId, VehicleDTO vehicleDTO) {
		if(cache.containsKey(vehicleId)) {
			cache.replace(vehicleId, vehicleDTO);
		} else {
			cache.put(vehicleId, vehicleDTO);
		}
	}

	public VehicleDTO getVehicleFromCache(String vehicleId) {
		return cache.get(vehicleId);
	}
}
