package by.bsuir.kirillpastukhou.impl.controllers;

import by.bsuir.kirillpastukhou.impl.service.TweetService;

import by.bsuir.kirillpastukhou.impl.dto.TweetRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.TweetResponseTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @GetMapping("/tweets")
    public ResponseEntity<List<TweetResponseTo>> getAllTweets() {
        List<TweetResponseTo> tweetResponseToList = tweetService.getAll();
        return new ResponseEntity<>(tweetResponseToList, HttpStatus.OK);
    }

    @GetMapping("/tweets/{id}")
    public ResponseEntity<TweetResponseTo> getTweet(@PathVariable long id) {
        TweetResponseTo tweetResponseTo = tweetService.get(id);
        return new ResponseEntity<>(tweetResponseTo, tweetResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/tweets")
    public ResponseEntity<TweetResponseTo> createTweet(@RequestBody TweetRequestTo TweetTo) {
        TweetResponseTo addedTweet = tweetService.add(TweetTo);
        return new ResponseEntity<>(addedTweet, HttpStatus.CREATED);
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<TweetResponseTo> deleteTweet(@PathVariable long id) {
        TweetResponseTo deletedTweet = tweetService.delete(id);
        return new ResponseEntity<>(deletedTweet, deletedTweet == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/tweets")
    public ResponseEntity<TweetResponseTo> updateTweet(@RequestBody TweetRequestTo tweetRequestTo) {
        TweetResponseTo tweetResponseTo = tweetService.update(tweetRequestTo);
        return new ResponseEntity<>(tweetResponseTo, tweetResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
