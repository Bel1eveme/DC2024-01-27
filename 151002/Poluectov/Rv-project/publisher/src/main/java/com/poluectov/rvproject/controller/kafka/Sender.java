package com.poluectov.rvproject.controller.kafka;

import com.poluectov.rvproject.config.KafkaConfig;
import com.poluectov.rvproject.dto.KafkaMessageRequestTo;
import com.poluectov.rvproject.dto.KafkaMessageResponseTo;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiver;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiverMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class Sender {

    @Value("${kafka.topic.message.request}")
    public static String MESSAGE_REQUEST_TOPIC;

    public static Long timeout = 10000L;

    KafkaSendReceiverMap<UUID> sendReceiverMap;

    private final KafkaTemplate<String, KafkaMessageRequestTo> template;

    public Sender(KafkaTemplate<String, KafkaMessageRequestTo> template,
                  KafkaSendReceiverMap<UUID> sendReceiverMap) {
        this.template = template;
        this.sendReceiverMap = sendReceiverMap;
    }
    public void send(String topic, KafkaMessageRequestTo message) {

        UUID id = message.getId();

        log.info("KAFKA: Sending message: {}", message);
        var msg = MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic);
        if (id != null) {
            msg.setHeader(KafkaHeaders.KEY, id.toString());
        }
        Message<KafkaMessageRequestTo> msgToSend = msg.build();

        template.send(msgToSend);
    }
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(KafkaConfig.class);


        //context.getBean(Sender.class).send("message-request", "test-key", error);
    }

    public KafkaMessageResponseTo sendAndReceive(String topic, KafkaMessageRequestTo message) throws TimeoutException {
        UUID requestId = UUID.randomUUID();

        while (sendReceiverMap.containsKey(requestId)) {
            requestId = UUID.randomUUID();
        }

        message.setId(requestId);

        this.send(topic, message);

        Future<KafkaSendReceiver<?>> future = sendReceiverMap.add(requestId, timeout);

        KafkaMessageResponseTo response;
        try {
            response = (KafkaMessageResponseTo) future.get().getMessage();
        }catch (TimeoutException e) {
            throw new TimeoutException("Timeout: " + e);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to send message: " + e);
        } finally {
            sendReceiverMap.remove(requestId);
        }
        return response;
    }
}
