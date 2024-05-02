package org.education.controller;


import jakarta.validation.Valid;
import org.education.bean.Message;
import org.education.bean.dto.MessageRequest;
import org.education.bean.dto.MessageRequestTo;
import org.education.bean.dto.MessageResponse;
import org.education.bean.dto.MessageResponseTo;
import org.education.exception.AlreadyExists;
import org.education.exception.ErrorResponse;
import org.education.exception.IncorrectValuesException;
import org.education.exception.NoSuchMessage;
import org.education.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Autowired
    public MessageController(MessageService messageService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @Autowired
    private KafkaTemplate<String, MessageResponse> messageKafkaTemplate;

    @Value("${kafka.query-topic}")
    private String queryTopic;

    @Value("${kafka.result-topic}")
    private String resultTopic;

    @KafkaListener(topics = "${kafka.query-topic}", groupId = "discussion-group",
            containerFactory = "userKafkaListenerContainerFactory")
    void listenerWithMessageConverter(MessageRequest messageMessage) {
        switch (messageMessage.getMessage()){
            case "GET" -> {
                if(messageMessage.getMessages().isEmpty()){
                    List<MessageResponseTo> response = new ArrayList<>();
                    for(Message message : messageService.getAll()){
                        response.add(modelMapper.map(message, MessageResponseTo.class));
                    }
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("SUCCESS", messageMessage.getIndex(), response));
                }
                else{
                    try{
                        Message message = messageService.getById(messageMessage.getMessages().get(0).getId());
                        messageKafkaTemplate.send(resultTopic, new MessageResponse("SUCCESS", messageMessage.getIndex(), List.of(modelMapper.map(message, MessageResponseTo.class))));
                    }
                    catch (NoSuchMessage e){
                        messageKafkaTemplate.send(resultTopic, new MessageResponse("NO SUCH MESSAGE", messageMessage.getIndex(), new ArrayList<>()));
                    }
                }
            }
            case "CREATE" -> {
                try{
                    Message message = messageService.create(modelMapper.map(messageMessage.getMessages().get(0), Message.class));
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("SUCCESS", messageMessage.getIndex(), List.of(modelMapper.map(message, MessageResponseTo.class))));
                }
                catch (AlreadyExists e){
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("ALREADY EXISTS", messageMessage.getIndex(), new ArrayList<>()));
                }
            }
            case "DELETE" -> {
                try{
                    messageService.delete(messageMessage.getMessages().get(0).getId());
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("SUCCESS", messageMessage.getIndex(), new ArrayList<>()));
                }
                catch (NoSuchMessage e){
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("NO SUCH MESSAGE", messageMessage.getIndex(), new ArrayList<>()));
                }
            }
            case "PUT" ->   {
                try{
                    Message message = messageService.update(modelMapper.map(messageMessage.getMessages().get(0), Message.class));
                    messageKafkaTemplate.send(resultTopic, new MessageResponse("SUCCESS", messageMessage.getIndex(), List.of(modelMapper.map(message, MessageResponseTo.class))));
                }
                catch (NoSuchMessage e){
                        messageKafkaTemplate.send(resultTopic, new MessageResponse("NO SUCH MESSAGE", messageMessage.getIndex(), new ArrayList<>()));
                }
            }
        }
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> getAllMessages(){
        List<MessageResponseTo> response = new ArrayList<>();
        for(Message message : messageService.getAll()){
            response.add(modelMapper.map(message, MessageResponseTo.class));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody @Valid MessageRequestTo messageRequestTo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new IncorrectValuesException("Incorrect input values");
        }
        Message message = messageService.create(modelMapper.map(messageRequestTo, Message.class));
        return new ResponseEntity<>(modelMapper.map(message, MessageResponseTo.class), HttpStatus.valueOf(201));
    }

    @PutMapping
    public ResponseEntity<MessageResponseTo> updateMessage(@RequestBody @Valid MessageRequestTo messageRequestTo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new IncorrectValuesException("Incorrect input values");
        }
        Message message = messageService.update(modelMapper.map(messageRequestTo, Message.class));
        return new ResponseEntity<>(modelMapper.map(message, MessageResponseTo.class), HttpStatus.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable int id){
        messageService.delete(id);
        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getMessage(@PathVariable int id){
        return new ResponseEntity<>(modelMapper.map(messageService.getById(id), MessageResponseTo.class), HttpStatus.valueOf(200));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(NoSuchMessage exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(IncorrectValuesException exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(AlreadyExists exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), 403),HttpStatus.valueOf(403));
    }
}