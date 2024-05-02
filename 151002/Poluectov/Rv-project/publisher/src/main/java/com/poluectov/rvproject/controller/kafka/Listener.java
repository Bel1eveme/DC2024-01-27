package com.poluectov.rvproject.controller.kafka;

import com.poluectov.rvproject.controller.redis.RedisMessagePublisher;
import com.poluectov.rvproject.dto.KafkaMessageResponseTo;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiver;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiverMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class Listener {

    private final KafkaSendReceiverMap<UUID> sendReceiverMap;

    private final RedisMessagePublisher redisMessagePublisher;


    @KafkaListener(topics = "${kafka.topic.message.response}")
    public void messageListener(KafkaMessageResponseTo response) {

        log.info("KAFKA: Message received: {}", response);

        UUID rqId = response.getRequestId();
        synchronized (sendReceiverMap) {
            if (sendReceiverMap.containsKey(rqId)) {
                KafkaSendReceiver<KafkaMessageResponseTo> stringSenderReceiver = (KafkaSendReceiver<KafkaMessageResponseTo>) sendReceiverMap.get(rqId);
                stringSenderReceiver.send(response);
            } else {
                //send to redis if another instance of publisher waits for this message
                redisMessagePublisher.publish(response);
            }
        }
    }
}
