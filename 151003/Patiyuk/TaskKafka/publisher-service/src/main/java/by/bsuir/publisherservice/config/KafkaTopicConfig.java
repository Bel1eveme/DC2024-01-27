package by.bsuir.publisherservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.request}")
    private String requestTopic;

    @Bean
    public NewTopic responseTopic() {
        return TopicBuilder
                .name(requestTopic)
                .partitions(3)
                .compact()
                .build();
    }
}
