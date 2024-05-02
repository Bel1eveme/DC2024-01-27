package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.TweetRequestDto;
import by.bsuir.publisher.dto.responses.TweetResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<TweetResponseDto> create(@RequestBody TweetRequestDto tweet) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.create(tweet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<TweetResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.readAll());
    }

    @PutMapping
    public ResponseEntity<TweetResponseDto> update(@RequestBody TweetRequestDto tweet) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.update(tweet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tweetService.delete(id));
    }
}
