package pl.kempa.saska.trackerservice.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import pl.kempa.saska.trackerservice.dto.VehicleDTO;

@Component
public class DistanceCalculator {

	public BigDecimal calculateDistance(VehicleDTO prev, VehicleDTO curr, String unit) {
		var lat1 = prev.latitude();
		var lat2 = curr.latitude();
		var lon1 = prev.longitude();
		var lon2 = curr.longitude();
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return new BigDecimal(0);
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(
					Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return new BigDecimal(dist);
		}
	}
}
