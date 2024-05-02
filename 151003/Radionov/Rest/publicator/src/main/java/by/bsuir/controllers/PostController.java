package by.bsuir.controllers;

import by.bsuir.dto.PostRequestTo;
import by.bsuir.dto.PostResponseTo;
import by.bsuir.exceptions.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    private RestClient restClient;
    @Autowired
    private KafkaConsumer<String, PostResponseTo> kafkaConsumer;
    @Autowired
    private KafkaSender kafkaSender;
    private String inTopic = "InTopic";
    private String outTopic = "OutTopic";
    private String uriBase = "http://localhost:24130/api/v1.0/posts";

    @GetMapping
    public ResponseEntity<List<?>> getPosts() {
        //kafkaSender.sendCustomMessage();
        return ResponseEntity.status(200).body(restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(List.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable Long id) throws NotFoundException {
        PostRequestTo postRequestTo = new PostRequestTo();
        postRequestTo.setMethod("GET");
        postRequestTo.setId(id);
        kafkaSender.sendCustomMessage(postRequestTo, inTopic);

        return ResponseEntity.status(200).body(listenKafka());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) throws NotFoundException {
        PostRequestTo postRequestTo = new PostRequestTo();
        postRequestTo.setMethod("DELETE");
        postRequestTo.setId(id);
        kafkaSender.sendCustomMessage(postRequestTo, inTopic);
        listenKafka();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<PostResponseTo> savePost(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody PostRequestTo post) throws NotFoundException {
        post.setCountry(acceptLanguageHeader);
        post.setMethod("POST");
        kafkaSender.sendCustomMessage(post, inTopic);
        return ResponseEntity.status(201).body(listenKafka());
    }

    @PutMapping()
    public ResponseEntity<PostResponseTo> updatePost(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody PostRequestTo post) throws NotFoundException {
        post.setCountry(acceptLanguageHeader);
        post.setMethod("PUT");
        kafkaSender.sendCustomMessage(post, inTopic);
        return ResponseEntity.status(200).body(listenKafka());
    }

    @GetMapping("/byTweet/{id}")
    public ResponseEntity<?> getAuthorByTweetId(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        return restClient.get()
                .uri(uriBase + "/byTweet/" + id)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(ResponseEntity.class);
    }
    private PostResponseTo listenKafka() throws NotFoundException {
        ConsumerRecords<String, PostResponseTo> records = kafkaConsumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, PostResponseTo> record : records) {

            String key = record.key();
            PostResponseTo value = record.value();
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
