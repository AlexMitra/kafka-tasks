package pl.kempa.saska.vehicleservice.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.vehicleservice.dto.VehicleDTO;
import pl.kempa.saska.vehicleservice.service.KafkaService;

@Service
@Slf4j
@AllArgsConstructor
public class VehicleCoordinatesService implements KafkaService<VehicleDTO> {

	private KafkaTemplate<String, VehicleDTO> kafkaTemplate;

	@Override
	@Transactional
	public void sendMessage(String topic, VehicleDTO vehicleDTO) {
		CompletableFuture<SendResult<String, VehicleDTO>> future =
				kafkaTemplate.send(topic, vehicleDTO.id().toString(), vehicleDTO);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("[VEHICLE SERVICE] Sent vehicle coordinates {} to partition {} with offset {}",
						vehicleDTO, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
			} else {
				log.info("[VEHICLE SERVICE] Unable to send vehicle coordinates {} due to {}",
						vehicleDTO, ex.getMessage());
			}
		});
	}
}
