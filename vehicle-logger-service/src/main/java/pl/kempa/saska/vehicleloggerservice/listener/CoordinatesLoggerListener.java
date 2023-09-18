package pl.kempa.saska.vehicleloggerservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.vehicleloggerservice.dto.VehicleDTO;

@Component
@Slf4j
public class CoordinatesLoggerListener {

	@KafkaListener(topics = {"${app.coordinates-logging-topic.name}"},
			groupId = "${spring.kafka.consumer.group-id}")
	public void onConsume(VehicleDTO vehicleDTO, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
		log.info("[LOGGER] Received vehicle with coordinates '{}' from [PARTITION] {}", vehicleDTO,
				partition);
	}
}
