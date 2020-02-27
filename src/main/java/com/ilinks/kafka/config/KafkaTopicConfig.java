package com.ilinks.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
	
	@Value(value = "${kafka.bootstrap.address}")
	private String bootstrapAddress;
	
	@Value(value = "${messaage.topic.name}")
	private String topic1;
	
	@Value(value = "${greetings.topic.name}")
	private String topic2;
	
	@Value(value = "${partitioned.topic.name}")
	private String topic3;
	
	@Value(value = "${filtered.topic.name}")
	private String topic4;
	
	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> config = new HashMap<>();
		config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(config);
	}
	
	@Bean 
	public NewTopic topic1() {
		return new NewTopic(topic1, 1, (short)1);
	}
	
	@Bean
	public NewTopic topic2() {
		return new NewTopic(topic2, 1, (short)1);
	}
	
	@Bean
	public NewTopic topic3() {
		return new NewTopic(topic3, 6, (short)1);
	}
	
	@Bean
	public NewTopic topic4() {
		return new NewTopic(topic4, 1, (short)1);
	}
	
}
