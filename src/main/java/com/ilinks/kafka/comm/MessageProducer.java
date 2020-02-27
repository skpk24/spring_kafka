package com.ilinks.kafka.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.ilinks.kafka.bean.Greeting;

public class MessageProducer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaMessageTemplate;
	
	@Autowired
	private KafkaTemplate<String, Greeting> greetingKafkaTemplate;
	
	@Value(value = "${messaage.topic.name}")
	private String topic1;
	
	@Value(value = "${greetings.topic.name}")
	private String topic2;
	
	@Value(value = "${partitioned.topic.name}")
	private String topic3;
	
	@Value(value = "${filtered.topic.name}")
	private String topic4;
	
	public void sendMessage(String message) {
		ListenableFuture<SendResult<String, String>> future = kafkaMessageTemplate.send(topic1, message);
		
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
	}
	
	public void sendMessageToPartion(String message, int partition) {
		kafkaMessageTemplate.send(topic3, partition, null, message);
    }
	
	public void sendMessageToFiltered(String message) {
		kafkaMessageTemplate.send(topic4, message);
    }

    public void sendGreetingMessage(Greeting greeting) {
        greetingKafkaTemplate.send(topic2, greeting);
    }
}
