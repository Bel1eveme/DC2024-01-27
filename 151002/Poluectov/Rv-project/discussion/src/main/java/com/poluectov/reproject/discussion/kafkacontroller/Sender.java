package com.poluectov.reproject.discussion.kafkacontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poluectov.reproject.discussion.config.KafkaConfig;
import com.poluectov.reproject.discussion.model.KafkaMessageResponseTo;
import com.poluectov.reproject.discussion.service.kafka.KafkaSendReceiverMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class Sender {

    @Value("${kafka.topic.message.request}")
    public static String MESSAGE_REQUEST_TOPIC;

    public static Long timeout = 1000L;
    ObjectMapper mapper = new ObjectMapper();

    KafkaSendReceiverMap<UUID> sendReceiverMap;

    private final KafkaTemplate<String, KafkaMessageResponseTo> template;

    public Sender(KafkaTemplate<String, KafkaMessageResponseTo> template,
                  KafkaSendReceiverMap<UUID> sendReceiverMap) {
        this.template = template;
        this.sendReceiverMap = sendReceiverMap;
    }

    public void send(String topic, KafkaMessageResponseTo message) {

        log.info("Sending message: {}", message);

        Message<KafkaMessageResponseTo> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, message.getRequestId().toString())
                .build();
        template.send(msg);
    }
}
