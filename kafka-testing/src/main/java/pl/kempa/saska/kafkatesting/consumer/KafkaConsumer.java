package pl.kempa.saska.kafkatesting.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumer<T>{

	void onConsume(T payload, String topic) ;
}
