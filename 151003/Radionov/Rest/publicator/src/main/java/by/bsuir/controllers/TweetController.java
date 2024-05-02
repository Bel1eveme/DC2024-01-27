package by.bsuir.controllers;

import by.bsuir.dto.TweetRequestTo;
import by.bsuir.dto.TweetResponseTo;
import by.bsuir.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {
    @Autowired
    TweetService tweetService;

    @GetMapping
    public ResponseEntity<List<TweetResponseTo>> getTweets(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ResponseEntity.status(200).body(tweetService.getTweets(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseTo> getTweet(@PathVariable int id) {
        return ResponseEntity.status(200).body(tweetService.getTweetById((long) id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id) {
        tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TweetResponseTo> saveTweet(@RequestBody TweetRequestTo tweet) {
        TweetResponseTo savedTweet = tweetService.saveTweet(tweet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTweet);
    }

    @PutMapping()
    public ResponseEntity<TweetResponseTo> updateTweet(@RequestBody TweetRequestTo tweet) {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.updateTweet(tweet));
    }

    @GetMapping("/byCriteria")
    public ResponseEntity<List<TweetResponseTo>> getTweetByCriteria(
            @RequestParam(required = false) List<String> stickerName,
            @RequestParam(required = false) List<Long> stickerId,
            @RequestParam(required = false) String authorLogin,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}