package messagesConsumer;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;


public class ConsumerTopic {
	
	/*Closing the constructor*/
	private ConsumerTopic(){}	
	
		
	private KafkaConsumer<String, Integer> consumer; 
	protected String topic;
	protected Properties kafkaProps;
	

	public void startReading() {

		final Thread mainThread = Thread.currentThread();

		// Registering a shutdown hook so we can exit cleanly
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Starting exit...");
				// Note that shutdownhook runs in a separate thread, so the only
				// thing we can safely do to a consumer is wake it up
				consumer.wakeup();
				try {
					mainThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			consumer = new KafkaConsumer<>(kafkaProps);
			consumer.subscribe(Collections.singletonList(topic));	

			// looping until ctrl-c, the shutdown hook will cleanup on exit
			while (true) {
				ConsumerRecords<String, Integer> records = consumer.poll(1000);
				System.out.println(System.currentTimeMillis() + "  --  waiting for data...");
				for (ConsumerRecord<String, Integer> record : records) {
					System.out.printf("offset = %d\n", record.offset());
					System.out.println("id= " + record.key() + ", operValue= " + record.value());
				}
				for (TopicPartition tp : consumer.assignment())
					System.out.println("Committing offset at position:" + consumer.position(tp));
				consumer.commitSync();
			}
		} catch (WakeupException e) {
			// ignore for shutdown
			
		} finally {
			consumer.close();
			System.out.println("Closed consumer and we are done");
		}
	}
	
	public static ConsumerTopic ConsumerTopicBuilder(String brokerServer, String topic, String groupId){
		ConsumerTopic consumerTopic = new ConsumerTopic();
		consumerTopic.topic = topic;
		consumerTopic.kafkaProps = new Properties();
		consumerTopic.kafkaProps.put("group.id", groupId);
		consumerTopic.kafkaProps.put("bootstrap.servers", brokerServer);
		consumerTopic.kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerTopic.kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return consumerTopic;		
	}

	
	
	

}
