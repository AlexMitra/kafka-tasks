package pl.kempa.saska.vehicleservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.kempa.saska.vehicleservice.dto.VehicleDTO;
import pl.kempa.saska.vehicleservice.service.KafkaService;

@RestController
@RequestMapping(value = "/api/vehicles-coordinates")
public class VehicleCoordinatesController {

	@Value("${app.vehicle-coordinates-topic.name}")
	private String topic;
	@Autowired private KafkaService kafkaService;

	@PostMapping
	public ResponseEntity<?> publish(@RequestBody VehicleDTO vehicleDTO) {
		if (vehicleDTO.id() == null) {
			return ResponseEntity.badRequest().body("Vehicle Id should be provided");
		}
		if (vehicleDTO.latitude() == null || vehicleDTO.longitude() == null) {
			return ResponseEntity.badRequest().body("Vehicle coordinates should be provided");
		}
		kafkaService.sendMessage(topic, vehicleDTO);
		return ResponseEntity.ok().build();
	}
}
