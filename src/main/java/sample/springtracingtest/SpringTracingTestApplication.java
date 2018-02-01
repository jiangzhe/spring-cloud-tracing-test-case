package sample.springtracingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableScheduling
@EnableConfigurationProperties(value = {
		KafkaProperties.class
})
public class SpringTracingTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTracingTestApplication.class, args);
	}
}
