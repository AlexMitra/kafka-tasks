package pl.kempa.saska.vehicleservice.config;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

public class VehiclePartitioner implements Partitioner {
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,
	                     Cluster cluster) {
		int chosenPartition;

		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();

		var keyNumber = Long.valueOf(key.toString());
		if (keyNumber < 10_000) {
			chosenPartition = 0;
		} else if (keyNumber < 20_000) {
			chosenPartition = 1;
		} else {
			chosenPartition = 2;
		}
		return chosenPartition;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> map) {

	}
}
