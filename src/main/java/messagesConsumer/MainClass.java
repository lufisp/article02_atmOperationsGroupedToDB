package messagesConsumer;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import kafka.consumer.ConsumerTopicMetrics;




/*Utilization example
mvn clean package	

java -jar target/article02_toatmOperationsWebService-0.0.1-SNAPSHOT.jar localhost:9092 atmOperationsToWebService gp01

*/


@Configuration
@EnableAutoConfiguration
@ComponentScan("restWebService")
public class MainClass {
	
	public ConsumerTopic toWebServiceConsumer;
	
	
	public static void main(String[] args) {
		
		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.ERROR);
		
		System.out.println("Start reading kafka queue...");
		MainClass mainClass = new MainClass();
		
		String brokerList = args[0]; 
		String topic = args[1]; 
		String groupId = args[2]; 
		
		
		mainClass.toWebServiceConsumer = ConsumerTopic.ConsumerTopicBuilder(brokerList, topic, groupId);
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		executorService.execute(mainClass.toWebServiceConsumer);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run(){
				System.out.println("Starting exit...");
				mainClass.toWebServiceConsumer.consumer.wakeup();
			}
		});
		
		
		System.out.println("Passando...");
	    SpringApplication.run(MainClass.class, args);

	}

}
