package pl.kempa.saska.trackerservice.service.impl;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void sendMessage(String topic, DistanceDTO distanceDTO) {
		kafkaTemplate.send(topic, UUID.randomUUID().toString(), distanceDTO);
	}
}
