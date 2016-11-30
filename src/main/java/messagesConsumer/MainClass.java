package messagesConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



/*
 * Util: mvn clean package
 * >>java -jar target/article02_toatmOperationsWebService-0.0.1-SNAPSHOT.jar

 * */

@Configuration
@EnableAutoConfiguration
@ComponentScan("restWebService")
public class MainClass {
	
	
	ConsumerTopic toHbaseConsumer;
	
	/*Utilization example
	mvn clean package	
	java -jar target/uber-article02_toatmOperationsGroupedToDB-0.0.1-SNAPSHOT.jar localhost:9092 atmOperationsGrouped gp01
    */
	
	
	public static void main(String[] args) {
		System.out.println("Start reading kafka queue...");
		MainClass mainClass = new MainClass();
		
		//String brokerList = args[0]; //"localhost:9092";
		//String topic = args[1]; //"atmOperations";
		//String groupId = args[2]; //"gp01";
		
		
		//mainClass.toHbaseConsumer = ConsumerTopic.ConsumerTopicBuilder(brokerList, topic, groupId);
		//mainClass.toHbaseConsumer.startReading();
	    SpringApplication.run(MainClass.class, args);

	}

}
