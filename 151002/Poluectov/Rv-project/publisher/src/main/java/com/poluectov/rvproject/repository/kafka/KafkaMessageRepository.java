package com.poluectov.rvproject.repository.kafka;

import com.poluectov.rvproject.dto.KafkaMessageRequestTo;
import com.poluectov.rvproject.dto.KafkaMessageResponseTo;
import com.poluectov.rvproject.controller.kafka.Sender;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.model.RestError;
import com.poluectov.rvproject.repository.MessageRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeoutException;

import static com.poluectov.rvproject.repository.kafka.KafkaMessageType.*;

@Service
public class KafkaMessageRepository implements MessageRepository {

    Sender sender;

    String responseTopic;

    String requestTopic;

    public KafkaMessageRepository(Sender sender,
                                  @Value("${kafka.topic.message.response}") String responseTopic,
                                  @Value("${kafka.topic.message.request}$") String requestTopic) {
        this.sender = sender;
        this.responseTopic = responseTopic;
        this.requestTopic = requestTopic;
    }

    @Override
    public List<Message> findAll() throws EntityNotFoundException {

        KafkaMessageRequestTo kafkaMessageRequestTo = KafkaMessageRequestTo.builder()
                .method(GET_ALL.getValue())
                .build();

        KafkaMessageResponseTo message = null;
        try {
            message = sender.sendAndReceive("message-request", kafkaMessageRequestTo);
        }catch (TimeoutException e){
            throw new RuntimeException("Response took too long");
        }
        handleException(message);
        return message.getMessages();
    }

    @Override
    public Message save(Message entity) throws EntityNotFoundException {
        entity.setId(getId());
        KafkaMessageRequestTo kafkaMessageRequestTo = KafkaMessageRequestTo.builder()
                .method(SAVE.getValue())
                .body(entity)
                .build();

        sender.send("message-request", kafkaMessageRequestTo);

        return entity;
    }

    @Override
    public void deleteById(Long aLong) throws EntityNotFoundException {
        KafkaMessageRequestTo kafkaMessageRequestTo = KafkaMessageRequestTo.builder()
                .method(DELETE.getValue())
                .params(Map.of("id", aLong.toString()))
                .build();

        KafkaMessageResponseTo message = null;
        try {
            message = sender.sendAndReceive("message-request", kafkaMessageRequestTo);
        }catch (TimeoutException e){
            throw new RuntimeException("Response took too long");
        }

        handleException(message);
    }

    @Override
    public Optional<Message> findById(Long aLong) throws EntityNotFoundException {

        KafkaMessageRequestTo kafkaMessageRequestTo = KafkaMessageRequestTo.builder()
                .method(ONE.getValue())
                .params(Map.of("id", aLong.toString()))
                .build();

        KafkaMessageResponseTo message = null;
        try {
            message = sender.sendAndReceive("message-request", kafkaMessageRequestTo);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (message.getStatus().equals("error") && message.getError().getStatus().equals(HttpStatus.NOT_FOUND)) {
            return Optional.empty();
        }

        handleException(message);
        return Optional.ofNullable(message.getMessages().get(0));
    }

    @Override
    public Message update(Message message) {

        KafkaMessageRequestTo kafkaMessageRequestTo = KafkaMessageRequestTo.builder()
                .method(UPDATE.getValue())
                .body(message)
                .build();

        KafkaMessageResponseTo kafkaMessageResponseTo = null;
        try {
            kafkaMessageResponseTo = sender.sendAndReceive("message-request", kafkaMessageRequestTo);
        }catch (Exception e){
            e.printStackTrace();
        }

        handleException(kafkaMessageResponseTo);
        return kafkaMessageResponseTo.getMessages().get(0);
    }

    private void handleException(KafkaMessageResponseTo response){
        if (response.getStatus().equals("error")) {
            RestError error = response.getError();
            if (error == null || error.getStatus() == null) {
                throw new RuntimeException("Unknown error. Error not specified");
            }

            if (error.getStatus().equals(HttpStatus.BAD_REQUEST)){
                throw new ValidationException(error.getMessage());
            }

            if (error.getStatus().equals(HttpStatus.NOT_FOUND)){
                throw new EntityNotFoundException(error.getMessage());
            }
        }
    }

    private Long getId (){
        int currentSecond = (int) (System.currentTimeMillis() / 1000);

        int shiftedTime = currentSecond << 10;

        int randomBits = new Random().nextInt(1 << 10);

        return (long) Math.abs(shiftedTime | randomBits);
    }
}
