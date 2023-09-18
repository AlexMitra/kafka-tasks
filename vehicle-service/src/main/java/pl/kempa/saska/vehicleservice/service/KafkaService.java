package pl.kempa.saska.vehicleservice.service;

import pl.kempa.saska.vehicleservice.dto.VehicleDTO;

public interface KafkaService<T> {
	void sendMessage(String topic, T dto);
}
