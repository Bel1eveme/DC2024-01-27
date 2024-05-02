package org.education.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.education.bean.Message;
import org.education.bean.dto.MessageRequest;
import org.education.bean.dto.MessageResponse;
import org.education.bean.dto.MessageRequestTo;
import org.education.exception.AlreadyExists;
import org.education.exception.IncorrectValuesException;
import org.education.exception.NoSuchMessage;
import org.education.exception.NoSuchIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@CacheConfig(cacheNames = "messageCache")
public class MessageService {

    private static final AtomicInteger index = new AtomicInteger(0);
    private static final AtomicInteger messageIndex = new AtomicInteger(0);
    private final ObjectMapper objectMapper;
    private final IssueService issueService;

    @Autowired
    private KafkaTemplate<String, MessageRequest> messageKafkaTemplate;

    @Value("${kafka.query-topic}")
    private String queryTopic;

    @Value("${kafka.result-topic}")
    private String resultTopic;

    public MessageService(RestTemplate restTemplate, ObjectMapper objectMapper, IssueService issueService) {
        this.objectMapper = objectMapper;
        this.issueService = issueService;
    }

    ConcurrentHashMap<Integer, CompletableFuture<MessageResponse>> taskMap = new ConcurrentHashMap<>();
    @KafkaListener(topics = "${kafka.result-topic}", groupId = "publicator-group",
            containerFactory = "userKafkaListenerContainerFactory")
    void listenerWithMessageConverter(MessageResponse messageMessage) {
        CompletableFuture<MessageResponse> task = taskMap.getOrDefault(messageMessage.getIndex(), null);
        if(task != null){
            task.complete(messageMessage);
        }
        taskMap.remove(messageMessage.getIndex());
    }

    @Cacheable(cacheNames = "messages")
    public List<Message> getAll(){
        int ind = messageIndex.decrementAndGet();
        CompletableFuture<MessageResponse> resultTask = new CompletableFuture<>();
        messageKafkaTemplate.send(queryTopic, "get", new MessageRequest("GET",ind , new ArrayList<>()));
        taskMap.put(ind, resultTask);
        try {
            MessageResponse res = resultTask.get(1000, TimeUnit.MILLISECONDS);
            if (res.getMessage().equals("SUCCESS")){
                return res.getMessages();
            }
            else{
                throw new RuntimeException(res.getMessage());
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Cacheable(cacheNames = "messages", key = "#id", unless = "#result == null")
    public Message getById(int id){

        MessageRequestTo temp = new MessageRequestTo();
        temp.setId(id);

        int ind = messageIndex.incrementAndGet();
        CompletableFuture<MessageResponse> resultTask = new CompletableFuture<>();
        messageKafkaTemplate.send(queryTopic, "get", new MessageRequest("GET",ind ,List.of(temp)));
        taskMap.put(ind, resultTask);
        try {
            MessageResponse res = resultTask.get(1000, TimeUnit.SECONDS);
            if (res.getMessage().equals("SUCCESS")){
                return res.getMessages().get(0);
            }
            else{
                if(res.getMessage().equals("NO SUCH MESSAGE")){
                    throw new NoSuchMessage(res.getMessage());
                }
                throw new RuntimeException(res.getMessage());
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @CacheEvict(cacheNames = "messages", allEntries = true)
    public Message create(MessageRequestTo message){
        if(!issueService.existsWithId(message.getIssueId())) throw new NoSuchIssue("No such issue with this id");
        message.setId(index.incrementAndGet());

        int ind = messageIndex.incrementAndGet();
        CompletableFuture<MessageResponse> resultTask = new CompletableFuture<>();
        taskMap.put(ind, resultTask);
        messageKafkaTemplate.send(queryTopic, String.valueOf(message.getId()), new MessageRequest("CREATE", ind,List.of(message)));
        try {
            MessageResponse res = resultTask.get(1, TimeUnit.SECONDS);
            if (res.getMessage().equals("SUCCESS")){
                    System.out.println(res.getMessages().get(0));
                return res.getMessages().get(0);
            }
            else{
                if(res.getMessage().equals("ALREADY EXISTS")){
                    throw new AlreadyExists(res.getMessage());
                }
                throw new RuntimeException(res.getMessage());
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @CacheEvict(cacheNames = "messages", allEntries = true)
    public Message update(MessageRequestTo message){
        int ind = messageIndex.incrementAndGet();
        CompletableFuture<MessageResponse> resultTask = new CompletableFuture<>();
        taskMap.put(ind, resultTask);
        messageKafkaTemplate.send(queryTopic, String.valueOf(message.getId()), new MessageRequest("PUT", ind,List.of(message)));
        try {
            MessageResponse res = resultTask.get(1000, TimeUnit.SECONDS);
            if (res.getMessage().equals("SUCCESS")){
                return res.getMessages().get(0);
            }
            else{
                if(res.getMessage().equals("NO SUCH MESSAGE")){
                    throw new NoSuchMessage(res.getMessage());
                }
                throw new RuntimeException(res.getMessage());
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Caching(evict = { @CacheEvict(cacheNames = "messages", key = "#id"),
            @CacheEvict(cacheNames = "messages", allEntries = true) })
    public void delete(int id){
        MessageRequestTo temp = new MessageRequestTo();
        temp.setId(id);
        int ind = messageIndex.incrementAndGet();
        CompletableFuture<MessageResponse> resultTask = new CompletableFuture<>();
        taskMap.put(ind, resultTask);
        messageKafkaTemplate.send(queryTopic, "delete", new MessageRequest("DELETE",ind,List.of(temp)));
        try {
            MessageResponse res = resultTask.get(1000, TimeUnit.SECONDS);
            if (!res.getMessage().equals("SUCCESS")){
                if(res.getMessage().equals("NO SUCH MESSAGE")){
                    throw new NoSuchMessage(res.getMessage());
                }
                throw new RuntimeException(res.getMessage());
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}