package pl.kempa.saska.trackerservice.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.kempa.saska.trackerservice.dto.VehicleDTO;

@Configuration
public class CacheConfig {

	@Bean
	public Map<String, VehicleDTO> cache() {
		return new ConcurrentHashMap<>();
	}
}
