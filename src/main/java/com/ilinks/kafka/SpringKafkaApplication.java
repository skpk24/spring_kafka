package com.ilinks.kafka;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.ilinks.kafka.bean.Greeting;
import com.ilinks.kafka.comm.MessageListener;
import com.ilinks.kafka.comm.MessageProducer;

@SpringBootApplication
public class SpringKafkaApplication {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(SpringKafkaApplication.class, args);
		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);
		
        producer.sendMessage("Hello, World!");
        listener.latch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to a topic with 5 partition,
         * each message to a different partition. But as per
         * listener configuration, only the messages from
         * partition 0 and 3 will be consumed.
         */
        for (int i = 0; i < 5; i++) {
            producer.sendMessageToPartion("Hello To Partioned Topic!", i);
        }
        listener.partitionLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'filtered' topic. As per listener
         * configuration,  all messages with char sequence
         * 'World' will be discarded.
         */
        producer.sendMessageToFiltered("Hello Pradeep !");
        producer.sendMessageToFiltered("Hello World!");
        listener.filterLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'greeting' topic. This will send
         * and recieved a java object with the help of 
         * greetingKafkaListenerContainerFactory.
         */
        producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
        listener.greetingLatch.await(10, TimeUnit.SECONDS);

        context.close();
	}

	@Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }
}
