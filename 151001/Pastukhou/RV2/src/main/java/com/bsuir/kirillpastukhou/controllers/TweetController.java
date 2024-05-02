package com.bsuir.kirillpastukhou.controllers;

import com.bsuir.kirillpastukhou.domain.request.TweetRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TweetResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.bsuir.kirillpastukhou.serivces.TweetService;

import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<TweetResponseTo> createTweet(@RequestBody TweetRequestTo tweetRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.create(tweetRequestTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseTo> findTweetById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.findTweetById(id));
    }

    @GetMapping
    public ResponseEntity<List<TweetResponseTo>> findAllTweets() {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.read());
    }

    @PutMapping
    public ResponseEntity<TweetResponseTo> updateTweet(@RequestBody TweetRequestTo tweetRequestTo) {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.update(tweetRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTweetById(@PathVariable Long id) {
        tweetService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }
}
