package messagesConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;

import model.DataModel;
import model.SingletonVariablesShare;


public class ConsumerTopic implements Runnable{
	
	
	/*Closing the constructor*/
	private ConsumerTopic(){}	
	
	public static final String USER_SCHEMA_WEBSERVICE = "{" + 
			"\"type\":\"record\"," + 
			"\"name\":\"atmRecord\"," + 
			"\"fields\":["	+ 
			"  { \"name\":\"id\", \"type\":\"string\" }," + 
			"  { \"name\":\"operValue\", \"type\":\"string\" }," + 
			"  { \"name\":\"lat\", \"type\":\"string\" }," +
			"  { \"name\":\"lng\", \"type\":\"string\" }" +
			"]}";
	
	protected KafkaConsumer<String, byte[]> consumer; 
	protected String topic;
	protected Properties kafkaProps;	
	
	protected int _SleepTime = 5000; 
	

	public void run() {


		try {
			consumer = new KafkaConsumer<>(kafkaProps);
			consumer.subscribe(Collections.singletonList(topic));	
			Schema.Parser parser = new Schema.Parser();
			Schema schema = parser.parse(USER_SCHEMA_WEBSERVICE);
			Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

			// looping until ctrl-c, the shutdown hook will cleanup on exit
			while (true) {
				List<DataModel> underConstruction = new ArrayList<>();
				ConsumerRecords<String, byte[]> records = consumer.poll(1000);
				System.out.println(System.currentTimeMillis() + "  --  waiting for data...");
				for (ConsumerRecord<String, byte[]> avroRecord : records) {
					GenericRecord record = recordInjection.invert(avroRecord.value()).get();
					String id = record.get("id").toString();
					String value = record.get("operValue").toString();
					String lat = record.get("lat").toString();
					String lng = record.get("lng").toString();
															
					System.out.println("id= " + id + ", operValue= " + value + ", lat: " + lat + " ,lng: " + lng);
					underConstruction.add(
							new DataModel(id,Integer.parseInt(value),lat,lng)
					);
				}
				SingletonVariablesShare.INSTANCE.setLastMessagesMicroBatch(underConstruction);				
				consumer.commitSync();
				Thread.sleep(_SleepTime);
			}
		} catch (WakeupException e) {
			// ignore for shutdown
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		consumerTopic.kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		
		return consumerTopic;		
	}

	

	
	
	

}
