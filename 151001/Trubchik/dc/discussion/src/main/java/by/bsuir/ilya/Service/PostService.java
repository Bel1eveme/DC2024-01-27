package by.bsuir.ilya.Service;


import by.bsuir.ilya.Entity.Issue;
import by.bsuir.ilya.Entity.Post;
import by.bsuir.ilya.dto.PostMapper;
import by.bsuir.ilya.dto.PostRequestTo;
import by.bsuir.ilya.dto.PostResponseTo;
import by.bsuir.ilya.repositories.PostRepository;
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
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private WebClient issueRepo = WebClient.create();
    public List<PostResponseTo> getAll() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            resultList.add(PostMapper.INSTANCE.postToPostResponseTo(postList.get(i)));
        }
        return resultList;
    }
    public List<PostRequestTo> getAllKafka() {
        List<Post> postList = postRepository.findAll();
        List<PostRequestTo> resultList = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            resultList.add(PostMapper.INSTANCE.postToPostRequestTo(postList.get(i)));
        }
        return resultList;
    }

    public PostResponseTo update(PostRequestTo updatingpost) {
        Post post =  PostMapper.INSTANCE.postRequestToToPost(updatingpost);
        if (validatePost(post)) {
            PostResponseTo responseTo;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                Optional<Issue> relatedIssue = issueRepo.get()
                        .uri("http://localhost:24110/api/v1.0/issues/" + post.getIssueId())
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(Issue.class)
                        .blockOptional();
                if (relatedIssue.isPresent()) {
                    return PostMapper.INSTANCE.postToPostResponseTo(postRepository.save(post));
                } else {
                    return new PostResponseTo();
                }
            } catch (WebClientResponseException e) {
                return new PostResponseTo();
            }
        }return new PostResponseTo();
    }
    public PostRequestTo updateKafka(PostRequestTo updatingpost) {
        return PostMapper.INSTANCE.postToPostRequestTo(postRepository.save(PostMapper.INSTANCE.postRequestToToPost(updatingpost)));
    }

    public PostResponseTo getById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return PostMapper.INSTANCE.postToPostResponseTo(post.get());
        } else {
            return new PostResponseTo();
        }
    }

    public PostRequestTo getByIdKafka(long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return PostMapper.INSTANCE.postToPostRequestTo(post.get());
        } else {
            return new PostRequestTo();
        }
    }


    public boolean deleteById(long id) {

        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            postRepository.delete(post.get());
            return  true;
        }
        return  false;
    }
    public PostRequestTo deleteByIdKafka(long id) {

        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            postRepository.delete(post.get());
            return  PostMapper.INSTANCE.postToPostRequestTo(post.get());
        }
        return  PostMapper.INSTANCE.postToPostRequestTo(null);
    }

    public ResponseEntity<PostResponseTo> add(PostRequestTo postRequestTo) {
        postRequestTo.setId(Math.abs(UUID.randomUUID().getLeastSignificantBits()) % 1000000);
        Post post =  PostMapper.INSTANCE.postRequestToToPost(postRequestTo);
        if (validatePost(post)) {
            PostResponseTo responseTo;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                Optional<Issue> relatedIssue = issueRepo.get()
                        .uri("http://localhost:24110/api/v1.0/issues/" + post.getIssueId())
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(Issue.class)
                        .blockOptional();
                if (relatedIssue.isPresent()) {
                    Post currPost = postRepository.save(post);
                    responseTo = PostMapper.INSTANCE.postToPostResponseTo(currPost);
                    return new ResponseEntity<>(responseTo,HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
                }
            } catch (WebClientResponseException e) {
                return new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
            }
        }return  new ResponseEntity<>(PostMapper.INSTANCE.postToPostResponseTo(post), HttpStatus.FORBIDDEN);
    }

    public PostRequestTo addKafka(PostRequestTo postRequestTo) {
        postRequestTo.setId(Math.abs(UUID.randomUUID().getLeastSignificantBits()) % 1000000);
        Post post =  PostMapper.INSTANCE.postRequestToToPost(postRequestTo);

        return  PostMapper.INSTANCE.postToPostRequestTo(postRepository.save(post));
    }

    private boolean validatePost(Post post) {
        if( post.getContent()!=null) {
            String content = post.getContent();
            if (content.length() >= 2 && content.length() <= 2048) return true;
        }
        return false;
    }
}
