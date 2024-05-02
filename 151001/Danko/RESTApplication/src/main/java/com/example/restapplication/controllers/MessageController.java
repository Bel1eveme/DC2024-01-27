package com.example.restapplication.controllers;

import com.example.restapplication.KafkaSender;
import com.example.restapplication.dto.MessageRequestTo;
import com.example.restapplication.dto.MessageResponseTo;
import com.example.restapplication.exceptions.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {

    @Autowired
    private RestClient restClient;

    @Autowired
    private KafkaConsumer<String, MessageResponseTo> kafkaConsumer;

    @Autowired
    private KafkaSender kafkaSender;

    private String kafkaTopic_inTopic = "InTopic";
    private String kafkaTopic_outTopic = "OutTopic";
    private String urlRebase = "http://localhost:24130/api/v1.0/messages";

    @GetMapping
    public ResponseEntity<List<?>> getAll() throws NotFoundException
    {
        return ResponseEntity.status(200).body(restClient.get().uri(urlRebase).retrieve().body(List.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getById(@PathVariable Long id) throws NotFoundException  {
        MessageRequestTo messageRequestTo = new MessageRequestTo();
        messageRequestTo.setId(id);
        messageRequestTo.setMethod("GET");
        kafkaSender.sendMessage(messageRequestTo, kafkaTopic_inTopic);
        return ResponseEntity.status(200).body(listenKafka());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseTo> delete(@PathVariable Long id) throws NotFoundException {
        MessageRequestTo messageRequestTo = new MessageRequestTo();
        messageRequestTo.setId(id);
        messageRequestTo.setMethod("DELETE");
        kafkaSender.sendMessage(messageRequestTo, kafkaTopic_inTopic);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(listenKafka());
    }

    @PostMapping
    public ResponseEntity<MessageResponseTo> save(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody MessageRequestTo message) throws NotFoundException {
        message.setMethod("POST");
        message.setCountry(acceptLanguageHeader);
        kafkaSender.sendMessage(message, kafkaTopic_inTopic);
        return ResponseEntity.status(201).body(listenKafka());
    }

    @PutMapping()
    public ResponseEntity<MessageResponseTo> update(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody MessageRequestTo message) throws NotFoundException  {
        message.setCountry(acceptLanguageHeader);
        message.setMethod("PUT");
        kafkaSender.sendMessage(message, kafkaTopic_inTopic);
        return ResponseEntity.status(200).body(listenKafka());
    }

    @GetMapping("/story/{id}")
    public ResponseEntity<?> getByStoryId(@RequestHeader HttpHeaders headers, @PathVariable Long id){
        return restClient.get()
                .uri(urlRebase + "/story/" + id)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(ResponseEntity.class);
    }

    private MessageResponseTo listenKafka() throws NotFoundException {
        ConsumerRecords<String, MessageResponseTo> records = kafkaConsumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, MessageResponseTo> record : records) {

            String key = record.key();
            MessageResponseTo value = record.value();
            if (value == null) {
                throw new NotFoundException("Not found", 40400L);
            }
            long offset = record.offset();
            int partition = record.partition();
            System.out.println("Received message: key = " + key + ", value = " + value +
                    ", offset = " + offset + ", partition = " + partition);

            return value;
        }
        return null;
    }
}
