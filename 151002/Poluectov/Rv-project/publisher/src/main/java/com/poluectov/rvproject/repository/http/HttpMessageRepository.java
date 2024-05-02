package com.poluectov.rvproject.repository.http;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.repository.MessageRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class HttpMessageRepository implements MessageRepository {

    WebClient.Builder webClientBuilder;

    String url;

    @Autowired
    public HttpMessageRepository(
            WebClient.Builder webClientBuilder,
            @Value("${url.message-service}") String url) {
        this.webClientBuilder = webClientBuilder;
        this.url = url;
    }


    @Override
    public List<Message> findAll() throws EntityNotFoundException {
        Optional<Message[]> messages = webClientBuilder.build()
                .get()
                .uri(url + "/messages")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    if (clientResponse.statusCode().value() == 404) {
                        throw new EntityNotFoundException("Messages not found");
                    }
                    throw new RuntimeException();
                })
                .bodyToMono(Message[].class)
                .blockOptional(Duration.ofSeconds(10));
        if (messages.isEmpty()){
            throw new RuntimeException("Response took too long");
        }
        return Arrays.stream(messages.get()).toList();
    }

    @Override
    public Message save(Message entity) throws EntityNotFoundException {

        Optional<Message> message = webClientBuilder.build()
                .post()
                .uri(url + "/messages")
                .body(Mono.just(entity), MessageRequestTo.class)
                .retrieve()
                .bodyToMono(Message.class)
                .blockOptional(Duration.ofSeconds(10));

        if(message.isPresent()){
            return message.get();
        }
        throw new EntityNotFoundException("Message not found");
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        URI uri = UriComponentsBuilder.fromHttpUrl(url).path("/messages/{id}").buildAndExpand(id).toUri();

        Optional<ResponseEntity<Void>> message = webClientBuilder.build()
                .delete()
                .uri(uri)
                .retrieve()
                .toBodilessEntity()
                .blockOptional(Duration.ofSeconds(10));

        if (message.isEmpty()){
            throw new RuntimeException("Response took too long");
        }
    }

    @Override
    public Optional<Message> findById(Long id) throws EntityNotFoundException {
        Optional<Message> message = webClientBuilder.build()
                .get()
                .uri(url + "/messages/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    if (clientResponse.statusCode().value() == 404) {
                        throw new EntityNotFoundException("Message not found");
                    }
                    throw new RuntimeException();
                })
                .bodyToMono(Message.class)
                .blockOptional(Duration.ofSeconds(10));

        if (message.isPresent()){
            return message;
        }
        throw new RuntimeException("Response took too long");
    }

    @Override
    public Message update(Message message) {
        Optional<Message> updated = webClientBuilder.build()
                .put()
                .uri(url + "/messages")
                .body(Mono.just(message), MessageRequestTo.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    if (clientResponse.statusCode().value() == 404) {
                        throw new EntityNotFoundException("Message not found");
                    }
                    throw new RuntimeException();
                })
                .bodyToMono(Message.class)
                .blockOptional(Duration.ofSeconds(10));

        if (updated.isPresent()){
            return updated.get();
        }
        throw new RuntimeException("Response took too long");
    }
}
