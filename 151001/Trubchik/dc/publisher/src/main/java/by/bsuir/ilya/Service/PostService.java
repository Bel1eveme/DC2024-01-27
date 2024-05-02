package by.bsuir.ilya.Service;


import by.bsuir.ilya.Entity.Issue;
import by.bsuir.ilya.Entity.Post;

import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.kafka.KafkaMessaging;
import by.bsuir.ilya.kafka.KafkaMethods;
import by.bsuir.ilya.kafka.KafkaRequest;
import by.bsuir.ilya.redis.RedisPostRepository;
import by.bsuir.ilya.repositories.IssueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostService implements IService<PostResponseTo, PostRequestTo> {
    private WebClient postRepository = WebClient.create();
    @Autowired
    private KafkaMessaging sender;
    @Autowired
    private IssueRepository  issueRepository;
    @Autowired
    private RedisPostRepository redisRepository;


    public List<PostResponseTo> getAll() {
        List<PostResponseTo> resultList;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            List<Post> posts = postRepository.get()
                    .uri("http://localhost:24130/api/v1.0/posts")
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .bodyToFlux(Post.class)
                    .collectList()
                    .block();
            resultList  = new ArrayList<>();
            for (Post post:
                 posts) {
                resultList.add(PostMapper.INSTANCE.postToPostResponseTo(post));
            }
        }catch (WebClientResponseException e) {
            return new ArrayList<>();
        }
        return resultList;
    }
    public List<PostResponseTo> getAllKafka() {
        List<Post> postList = redisRepository.getAll();;
        List<PostResponseTo> resultList = new ArrayList<>();
        if(!postList.isEmpty())
        {
            for (int i = 0; i < postList.size(); i++) {
                resultList.add(PostMapper.INSTANCE.postToPostResponseTo(postList.get(i)));
            }
            return  resultList;
        }
        else {
            List<PostRequestTo> result = new ArrayList<>();
            KafkaRequest request = new KafkaRequest();
            request.setKey(UUID.randomUUID());
            request.setRequestMethod(KafkaMethods.READ_ALL);
            try {
                result = sender.sendMessage(request);
                if (result.isEmpty()) {
                    return null;
                } else {
                    for(int i = 0; i<result.size();i++)
                    {
                        redisRepository.add(PostMapper.INSTANCE.postRequestToToPost(result.get(i)));
                    }
                    return PostMapper.INSTANCE.listRequestToResponse(result);
                }
            } catch (JsonProcessingException e) {
                return null;
            }
        }

    }

    public PostResponseTo update(PostRequestTo updatingpost) {
        Post post = PostMapper.INSTANCE.postRequestToToPost(updatingpost);
        if (validatePost(post)) {
            Optional<Issue> relatedIssue = issueRepository.findById(post.getIssueId());
            if (relatedIssue.isPresent()) {
                Optional<Post> redisPost = redisRepository.getById(post.getId());
                if (redisPost.isPresent() && post.equals(redisPost.get())) {
                    return PostMapper.INSTANCE.postToPostResponseTo(redisPost.get());
                } else {
                    List<PostRequestTo> result;
                    KafkaRequest request = new KafkaRequest();
                    request.setKey(UUID.randomUUID());
                    request.getDtoToTransfer().add(updatingpost);
                    request.setRequestMethod(KafkaMethods.UPDATE);
                    try {
                        result = sender.sendMessage(request);
                        if (!result.isEmpty()) {
                            Post resultPost = PostMapper.INSTANCE.postRequestToToPost(result.get(0));
                            redisRepository.update(resultPost);
                            return PostMapper.INSTANCE.postToPostResponseTo(PostMapper.INSTANCE.postRequestToToPost(result.get(0)));
                        }
                        return new PostResponseTo();
                    } catch (JsonProcessingException e) {
                        return new PostResponseTo();
                    }
                }
            }
            else{
                return new PostResponseTo();
            }

        }
        else  return new PostResponseTo();
    }

    public PostResponseTo getById(long id) {
        Optional<Post> redisPost = redisRepository.getById(id);
        if (redisPost.isPresent()) {
            return PostMapper.INSTANCE.postToPostResponseTo(redisPost.get());
        } else{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                Optional<Post> post = postRepository.get()
                        .uri("http://localhost:24130/api/v1.0/posts/" + id)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(Post.class)
                        .blockOptional();
                if (post.isPresent()) {
                    redisRepository.add(post.get());
                    return PostMapper.INSTANCE.postToPostResponseTo(post.get());
                } else {
                    return PostMapper.INSTANCE.postToPostResponseTo(new Post());
                }
            } catch (WebClientResponseException e) {
                return PostMapper.INSTANCE.postToPostResponseTo(new Post());
            }
        }
    }

    public PostResponseTo getByIdKafka(long id) {
        Optional<Post> redisPost = redisRepository.getById(id);
        if (redisPost.isPresent()) {
            return PostMapper.INSTANCE.postToPostResponseTo(redisPost.get());
        }else {
            PostRequestTo postRequestTo = new PostRequestTo();
            postRequestTo.setId(id);
            KafkaRequest request = new KafkaRequest();
            request.setKey(UUID.randomUUID());
            request.getDtoToTransfer().add(postRequestTo);
            request.setRequestMethod(KafkaMethods.READ);
            try {
                List<PostRequestTo> result = sender.sendMessage(request);
                if (!result.isEmpty()) {
                    redisRepository.add(PostMapper.INSTANCE.postRequestToToPost(result.get(0)));
                    return PostMapper.INSTANCE.postToPostResponseTo(PostMapper.INSTANCE.postRequestToToPost(result.get(0)));
                }
                ;
            } catch (JsonProcessingException e) {
                return null;
            }
            return null;
        }
    }

    public boolean deleteById(long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            Optional<Post> post = postRepository.get()
                    .uri("http://localhost:24130/api/v1.0/posts/" + id)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .bodyToMono(Post.class)
                    .blockOptional();
            if (post.isPresent()) {
                headers.setContentType(MediaType.APPLICATION_JSON);
                postRepository.delete()
                        .uri("http://localhost:24130/api/v1.0/posts/" + id)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(Post.class)
                        .block();
                return true;
            }else return false;
        }catch (WebClientResponseException e){
            return false;
        }
    }

    public boolean deleteByIdKafka(long id)
    {
        PostRequestTo postRequestTo = new PostRequestTo();
        postRequestTo.setId(id);
        KafkaRequest request= new KafkaRequest();
        request.setKey(UUID.randomUUID());
        request.getDtoToTransfer().add(postRequestTo);
        request.setRequestMethod(KafkaMethods.DELETE);
        try {
            List<PostRequestTo> result = sender.sendMessage(request);
            if(!result.isEmpty())
            {
                if(result.get(0) == null)
                {
                    return false;
                }
                else
                {
                    return  true;
                }
            }
        }catch (JsonProcessingException e){
            return false;
        }
        redisRepository.delete(id);
        return true;
    }
    public ResponseEntity<PostResponseTo> add(PostRequestTo postRequestTo) {
        Post post = PostMapper.INSTANCE.postRequestToToPost(postRequestTo);
        if (validatePost(post)) {
            Optional<Issue> relatedIssue = issueRepository.findById(post.getIssueId());
            if (relatedIssue.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                try {
                    Optional<Post> savedPost = postRepository.post()
                            .uri("http://localhost:24130/api/v1.0/posts")
                            .headers(httpHeaders -> httpHeaders.addAll(headers))
                            .bodyValue(post)
                            .retrieve()
                            .bodyToMono(Post.class)
                            .blockOptional();
                    return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(savedPost.get()), HttpStatus.CREATED);
                } catch (WebClientResponseException e) {
                    return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
            }
        }
        else return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<PostResponseTo> addKafka(PostRequestTo postRequestTo) {
        Post post = PostMapper.INSTANCE.postRequestToToPost(postRequestTo);
        if (validatePost(post)) {
            Optional<Issue> relatedIssue = issueRepository.findById(post.getIssueId());
            if (relatedIssue.isPresent()) {
                List<PostRequestTo> result;
                KafkaRequest request = new KafkaRequest();
                request.setKey(UUID.randomUUID());
                request.getDtoToTransfer().add(postRequestTo);
                request.setRequestMethod(KafkaMethods.CREATE);
                try {
                    result = sender.sendMessage(request);
                    if(!result.isEmpty())
                    {
                        redisRepository.add(PostMapper.INSTANCE.postRequestToToPost(result.get(0)));
                        return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(PostMapper.INSTANCE.postRequestToToPost(result.get(0))),HttpStatus.CREATED);
                    }
                    return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
                } catch (JsonProcessingException e) {
                    return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
            }
        }
        else  return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
    }

    private boolean validatePost(Post post) {
        if( post.getContent()!=null) {
            String content = post.getContent();
            if (content.length() >= 2 && content.length() <= 2048) return true;
        }
        return false;
    }
}
