package pl.kempa.saska.trackerservice.service;

public interface LoggingService<T> {

	void sendMessage(String topic, T dto);
}
