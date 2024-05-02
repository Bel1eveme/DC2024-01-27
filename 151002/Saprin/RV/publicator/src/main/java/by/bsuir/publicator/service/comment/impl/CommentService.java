package by.bsuir.publicator.service.comment.impl;

import by.bsuir.publicator.bean.Comment;
import by.bsuir.publicator.dto.CommentAddResponseTo;
import by.bsuir.publicator.dto.CommentRequestTo;
import by.bsuir.publicator.dto.CommentResponseTo;
import by.bsuir.publicator.exception.DuplicateEntityException;
import by.bsuir.publicator.exception.EntityNotFoundException;
import by.bsuir.publicator.producer.CommentProducer;
import by.bsuir.publicator.service.comment.ICommentService;
import by.bsuir.publicator.util.converter.comment.CommentConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@CacheConfig(cacheNames = "editorsCache")
public class CommentService implements ICommentService {

    private final CommentProducer commentProducer;
    private final CommentConverter commentConverter;
    private final String ENTITY_NAME = "comment";
    private final String URL_COMMENT = "http://localhost:24130/api/v1.0/comments";

    @Autowired
    public CommentService(CommentProducer commentProducer, CommentConverter commentConverter) {
        this.commentProducer = commentProducer;
        this.commentConverter = commentConverter;
    }
    @Override
    @Cacheable(cacheNames = "comments")
    public List<CommentResponseTo> getComments() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommentResponseTo[]> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<CommentResponseTo[]> response = restTemplate.exchange(URL_COMMENT, HttpMethod.GET, requestEntity, CommentResponseTo[].class);
        return new ArrayList<>(List.of(Objects.requireNonNull(response.getBody())));
    }

    @Override
    public CommentAddResponseTo addComment(CommentRequestTo comment) throws DuplicateEntityException, JsonProcessingException, InterruptedException {
        comment.setId(generateRandomBigInteger(32));
        ObjectMapper objectMapper = new ObjectMapper();
        CommentResponseTo commentResponseTo = commentProducer.sendRequest("POST", objectMapper.writeValueAsString(comment));
        return new CommentAddResponseTo(commentResponseTo.getId(), commentResponseTo.getIssueId(), commentResponseTo.getContent(), "PENDING");
    }


    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "comments", key = "#id"),
            @CacheEvict(cacheNames = "comments", allEntries = true) })
    public void deleteComment(BigInteger id) throws EntityNotFoundException, InterruptedException {
        commentProducer.sendRequest("DELETE", id.toString());
    }

    @Override
    @CacheEvict(cacheNames = "comments", allEntries = true)
    public CommentResponseTo updateComment(CommentRequestTo comment) throws EntityNotFoundException, JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        return commentProducer.sendRequest("PUT", objectMapper.writeValueAsString(comment));
    }

    @Override
    @Cacheable(cacheNames = "comments", key = "#id", unless = "#result == null")
    public CommentResponseTo getCommentById(BigInteger id) throws EntityNotFoundException, ExecutionException, InterruptedException {
        return commentProducer.sendRequest("GET", id.toString());
    }

    public static BigInteger generateRandomBigInteger(int numBits) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[numBits / 8];
        secureRandom.nextBytes(bytes);
        return new BigInteger(1, bytes);
    }
}
