package pl.kempa.saska.trackerservice.service.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.trackerservice.dto.DistanceDTO;
import pl.kempa.saska.trackerservice.service.LoggingService;

@Service
@Slf4j
@AllArgsConstructor
public class LoggingServiceImpl implements LoggingService<DistanceDTO> {

	private KafkaTemplate<String, DistanceDTO> kafkaTemplate;

	@Override
	public void sendMessage(String topic, DistanceDTO distanceDTO) {
		CompletableFuture<SendResult<String, DistanceDTO>> future =
				kafkaTemplate.send(topic, UUID.randomUUID().toString(), distanceDTO);
//		future.whenComplete((result, ex) -> {
//			if (ex == null) {
//				log.info("[TRACKER SERVICE] Sent vehicle distance {} for logging topic to partition {}",
//						distanceDTO, result.getRecordMetadata().partition());
//			} else {
//				log.info("[TRACKER SERVICE] Unable to send vehicle distance {} for logging topic due to {}",
//						distanceDTO, ex.getMessage());
//			}
//		});
	}
}
