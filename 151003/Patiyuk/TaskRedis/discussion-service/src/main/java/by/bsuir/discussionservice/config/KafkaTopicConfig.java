package by.bsuir.discussionservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.response}")
    private String responseTopic;

    @Value("${spring.kafka.topic.create}")
    private String createTopic;

    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder
                        .name(responseTopic)
                        .partitions(3)
                        .compact()
                        .build(),
                TopicBuilder
                        .name(createTopic)
                        .partitions(3)
                        .compact()
                        .build()
        );
    }
}
