package com.poluectov.reproject.discussion.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.url}")
    String kafkaUrl;

    @Value("${kafka.topic.message.request}")
    String messageRequestTopic;

    @Value("${kafka.topic.message.response}")
    String messageResponseTopic;

    @Bean
    public NewTopic RequestTopic() {
        return TopicBuilder.name(messageRequestTopic)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ResponseTopic() {
        return TopicBuilder.name(messageResponseTopic)
                .partitions(10)
                .replicas(1)
                .build();
    }
}
