package com.poluectov.rvproject.controller.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poluectov.rvproject.dto.KafkaMessageResponseTo;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiver;
import com.poluectov.rvproject.service.kafka.KafkaSendReceiverMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final KafkaSendReceiverMap<UUID> sendReceiverMap;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            KafkaMessageResponseTo response = objectMapper.readValue(message.toString(), KafkaMessageResponseTo.class);
            log.info("REDIS: Message received: {}", response);
            UUID rqId = response.getRequestId();
            synchronized (sendReceiverMap) {
                if (sendReceiverMap.containsKey(rqId)) {
                    KafkaSendReceiver<KafkaMessageResponseTo> stringSenderReceiver = (KafkaSendReceiver<KafkaMessageResponseTo>) sendReceiverMap.get(rqId);
                    stringSenderReceiver.send(response);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
