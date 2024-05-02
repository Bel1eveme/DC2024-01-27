package com.poluectov.reproject.discussion.kafkacontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poluectov.reproject.discussion.controller.MessageController;
import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import com.poluectov.reproject.discussion.model.KafkaMessageRequestTo;
import com.poluectov.reproject.discussion.model.KafkaMessageResponseTo;
import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.model.RestError;
import com.poluectov.reproject.discussion.repository.exception.EntityNotFoundException;
import com.poluectov.reproject.discussion.service.MessageService;
import com.poluectov.reproject.discussion.service.kafka.KafkaMessageType;
import com.poluectov.reproject.discussion.service.kafka.KafkaSendReceiverMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class Listener {

    ObjectMapper mapper;

    KafkaSendReceiverMap<UUID> sendReceiverMap;

    MessageService messageService;

    Sender sender;

    String responseTopic;

    @Autowired
    public Listener(ObjectMapper mapper,
                    KafkaSendReceiverMap<UUID> sendReceiverMap,
                    MessageService messageService,
                    Sender sender,
                    @Value("${kafka.topic.message.response}") String responseTopic) {
        this.mapper = mapper;
        this.sendReceiverMap = sendReceiverMap;
        this.messageService = messageService;
        this.sender = sender;
        this.responseTopic = responseTopic;
    }

    @KafkaListener(topics = "${kafka.topic.message.request}")
    public void messageListener(KafkaMessageRequestTo request) {

        log.info("Message received: {}", request);
        KafkaMessageResponseTo response = new KafkaMessageResponseTo();
        UUID requestId = request.getId();

        try {

            response.setRequestId(requestId);
            String method = request.getMethod();
            KafkaMessageType kafkaMessageType = KafkaMessageType.valueOf(method);
            switch (kafkaMessageType) {
                case GET_ALL -> {
                    getAll(response);
                    sender.send("message-response", response);
                }
                case ONE -> {
                    getOne(request, response);
                    sender.send("message-response", response);
                }
                case SAVE -> {
                    create(request);
                }
                case UPDATE -> {
                    update(request, response);
                    sender.send("message-response", response);
                }
                case DELETE -> {
                    delete(request, response);
                    sender.send("message-response", response);
                }
                default -> {
                    RestError error = RestError.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("Unknown method")
                            .build();
                    response.setError(error);
                    sender.send("message-response", response);
                }
            }
        }catch (Exception e){
            if (response.getStatus() == null || response.getError() == null){
                response.setStatus("error");
                response.setError(RestError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage()).build());
            }
            sender.send("message-response", response);
        }
    }

    private void getAll(KafkaMessageResponseTo response){
        List<MessageResponseTo> responseToList = messageService.all();

        response.setStatus("ok");
        response.setMessages(responseToList);
    }

    private void getOne(KafkaMessageRequestTo request, KafkaMessageResponseTo response){
        String idString = request.getParams().get("id");

        if (idString == null) {
            RestError error = RestError.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("id is null")
                    .build();
            log.info("id is null");
            response.setStatus("error");
            response.setError(error);
            return;
        }
        Long id = Long.valueOf(idString);

        Optional<MessageResponseTo> message = messageService.one(id);

        if (message.isEmpty()){
            RestError error = RestError.builder()
                            .status(HttpStatus.NOT_FOUND)
                                    .message("message not found")
                                            .build();

            log.info("message not found");
            response.setError(error);
            response.setStatus("error");
            return;
        }
        //set response message
        response.setStatus("ok");
        response.setMessages(List.of(message.get()));
    }

    private void create(KafkaMessageRequestTo request){
        MessageRequestTo requestBody = request.getBody();

        messageService.create(requestBody);
    }

    private void update(KafkaMessageRequestTo request, KafkaMessageResponseTo response){
        MessageRequestTo requestBody = request.getBody();

        if (requestBody == null || requestBody.getId() == null){
            messageNotFound(requestBody, response);
            return;
        }

        Optional<MessageResponseTo> responseTo = messageService.update(requestBody.getId(), requestBody);

        if (responseTo.isEmpty()){
            log.info("message not found");
            response.setStatus("error");
            RestError error = RestError.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("message not found")
                    .build();
            response.setError(error);
            return;
        }
        response.setStatus("ok");
        response.setMessages(List.of(responseTo.get()));
    }

    private void delete(KafkaMessageRequestTo request, KafkaMessageResponseTo response){
        String idString = request.getParams().get("id");
        Long id = Long.valueOf(idString);
        try {
            messageService.delete(id);
            response.setStatus("ok");
        }catch (EntityNotFoundException e){
            response.setStatus("error");
            RestError error = RestError.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(e.getMessage())
                    .build();
            response.setError(error);
        }
    }


    private void messageNotFound(MessageRequestTo requestBody, KafkaMessageResponseTo response){
        log.info("message not found");
        response.setStatus("error");
        RestError error = RestError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("message not found")
                .build();
        response.setError(error);
    }

}
