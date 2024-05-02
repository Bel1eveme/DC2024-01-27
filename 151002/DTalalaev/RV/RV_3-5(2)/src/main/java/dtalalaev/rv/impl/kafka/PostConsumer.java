package dtalalaev.rv.impl.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtalalaev.rv.impl.model.post.PostRequestTo;
import dtalalaev.rv.impl.model.post.PostResponseTo;
import dtalalaev.rv.impl.model.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class PostConsumer {


    private final PostProducer postProducer;
    private final PostService postService;

    @Autowired
    public PostConsumer(PostProducer postProducer, PostService postService) {
        this.postProducer = postProducer;
        this.postService = postService;
    }

    @KafkaListener(topics = "OutTopic", groupId = "my-group")
    public void consume(String message) throws JsonProcessingException {
        if (message.startsWith("GET:")) {
            String postId = message.substring(4);
            PostResponseTo postResponseTo = getPostResponseTo(new BigInteger(postId));
            postProducer.sendMessage(postResponseTo);
        } if (message.startsWith("POST:")) {
            String postJson = message.substring(5);
            PostRequestTo post = parseToPostRequestTo(postJson);
            PostResponseTo postResponseTo = postService.create(post);
            postProducer.sendMessage(postResponseTo);
        } if (message.startsWith("DELETE:")) {
            String postId = message.substring(7);
            postService.delete(new BigInteger(postId));
        } if (message.startsWith("PUT:")) {
            String postJson = message.substring(4);
            PostRequestTo post = parseToPostRequestTo(postJson);
            PostResponseTo postResponseTo = postService.update(post);
            postProducer.sendMessage(postResponseTo);
        }
    }

    private PostRequestTo parseToPostRequestTo(String post) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(post, PostRequestTo.class);

    }

    private PostResponseTo getPostResponseTo(BigInteger id) {
        return postService.findOne(id);
    }

}
