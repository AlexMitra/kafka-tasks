package pl.kempa.saska.trackerservice.service.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.trackerservice.dto.VehicleDTO;
import pl.kempa.saska.trackerservice.service.LoggingService;

@Service
@Slf4j
@AllArgsConstructor
public class LoggingServiceImpl implements LoggingService<VehicleDTO> {

	private KafkaTemplate<String, VehicleDTO> kafkaTemplate;

	@Override
	public void sendMessage(String topic, VehicleDTO vehicleDTO) {
		CompletableFuture<SendResult<String, VehicleDTO>> future =
				kafkaTemplate.send(topic, UUID.randomUUID().toString(), vehicleDTO);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("[TRACKER SERVICE] Sent vehicle coordinates {} for logging topic to partition {}",
						vehicleDTO, result.getRecordMetadata().partition());
			} else {
				log.info("[TRACKER SERVICE] Unable to send vehicle coordinates {} for logging topic due to {}",
						vehicleDTO, ex.getMessage());
			}
		});
	}
}
