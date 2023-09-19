package pl.kempa.saska.vehicleloggerservice.listener;

import java.text.DecimalFormat;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.kempa.saska.vehicleloggerservice.dto.DistanceDTO;

@Component
@Slf4j
public class CoordinatesLoggerListener {

	private DecimalFormat df = new DecimalFormat("###.#####");

	@KafkaListener(topics = {"${app.coordinates-logging-topic.name}"},
			groupId = "${spring.kafka.consumer.group-id}")
	public void onConsume(DistanceDTO distanceDTO) {
		log.info("[LOGGER] vehicle {} pased  distance {} km", distanceDTO.id(),
				df.format(distanceDTO.distance()));
	}
}
