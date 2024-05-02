package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.tweet.*;
import org.example.publisher.impl.tweet.dto.TweetRequestTo;
import org.example.publisher.impl.tweet.dto.TweetResponseTo;
import org.example.publisher.impl.tweet.service.TweetService;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseTo> getTweets() {
        return tweetService.getTweets();
    }

    @GetMapping("/{id}")
    public TweetResponseTo getTweetById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return tweetService.getTweetById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseTo createTweet(@Valid @RequestBody TweetRequestTo tweetRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        return tweetService.saveTweet(tweetRequestTo);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseTo updateIssue(@Valid @RequestBody TweetRequestTo tweet) throws EntityNotFoundException, DuplicateEntityException {
        return tweetService.updateIssue(tweet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable BigInteger id) throws EntityNotFoundException {
        tweetService.deleteIssue(id);
    }
}
