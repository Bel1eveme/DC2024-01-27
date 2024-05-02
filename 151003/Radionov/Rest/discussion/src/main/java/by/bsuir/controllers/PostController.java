package by.bsuir.controllers;

import by.bsuir.dto.PostRequestTo;
import by.bsuir.dto.PostResponseTo;
import by.bsuir.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private KafkaSender kafkaSender;
    private String topic = "OutTopic";

    @KafkaListener(topics = "InTopic", groupId = "inGroup",
            containerFactory = "postRequestToConcurrentKafkaListenerContainerFactory")
    void listenerWithMessageConverter(@Payload PostRequestTo postRequestTo) {
        if (Objects.equals(postRequestTo.getMethod(), "GET")) {
            if (postRequestTo.getId() != null) {
                kafkaSender.sendCustomMessage(getPost(postRequestTo.getId()), topic);
            } else {
                // kafkaSender.sendCustomMessage(getPosts());
            }
        } else {
            if (Objects.equals(postRequestTo.getMethod(), "DELETE")) {
                kafkaSender.sendCustomMessage(deletePost(postRequestTo.getId()), topic);
            } else {
                if (Objects.equals(postRequestTo.getMethod(), "POST")) {
                    kafkaSender.sendCustomMessage(savePost(postRequestTo.getCountry(), postRequestTo), topic);
                } else {
                    if (Objects.equals(postRequestTo.getMethod(), "PUT")) {
                        kafkaSender.sendCustomMessage(updatePost(postRequestTo.getCountry(), postRequestTo), topic);
                    }
                }
            }
        }
    }

    @GetMapping
    public List<PostResponseTo> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public PostResponseTo getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public PostResponseTo deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new PostResponseTo();
    }

    @PostMapping
    public PostResponseTo savePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        return postService.savePost(post, acceptLanguageHeader);
    }

    @PutMapping()
    public PostResponseTo updatePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        return postService.updatePost(post, acceptLanguageHeader);
    }

    @GetMapping("/byTweet/{id}")
    public List<PostResponseTo> getAuthorByTweetId(@PathVariable Long id) {
        return postService.getPostByTweetId(id);
    }
}
