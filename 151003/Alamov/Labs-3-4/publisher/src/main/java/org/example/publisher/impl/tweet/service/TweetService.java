package org.example.publisher.impl.tweet.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.editor.Editor;
import org.example.publisher.impl.editor.EditorRepository;
import org.example.publisher.impl.tweet.Tweet;
import org.example.publisher.impl.tweet.TweetRepository;
import org.example.publisher.impl.tweet.dto.TweetRequestTo;
import org.example.publisher.impl.tweet.dto.TweetResponseTo;
import org.example.publisher.impl.tweet.mapper.Impl.TweetMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final EditorRepository editorRepository;
    private final TweetMapperImpl tweetMapper;

    private final String ENTITY_NAME = "tweet";


    public List<TweetResponseTo> getTweets() {
        List<Tweet> tweets = tweetRepository.findAll();
        List<TweetResponseTo> tweetResponseTos = new ArrayList<>();

        for (var item : tweets) {
            tweetResponseTos.add(tweetMapper.tweetToResponseTo(item));
        }
        return tweetResponseTos;
    }


    public TweetResponseTo getTweetById(BigInteger id) throws EntityNotFoundException {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if (tweet.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return tweetMapper.tweetToResponseTo(tweet.get());
    }

    public TweetResponseTo saveTweet(TweetRequestTo tweetRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Editor> editor = editorRepository.findById(tweetRequestTo.getEditorId());
        if (editor.isEmpty()) {
            throw new EntityNotFoundException("Editor", tweetRequestTo.getEditorId());
        }

        if (tweetRequestTo.getCreated() == null) {
            tweetRequestTo.setCreated(new Date());
        }
        if (tweetRequestTo.getModified() == null) {
            tweetRequestTo.setModified(new Date());
        }
        if (isNumeric(tweetRequestTo.getContent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "iss_content should be a string, not a number.");
        }
        Tweet issueEntity = tweetMapper.dtoToEntity(tweetRequestTo, editor.get());

        try {
            Tweet savedIssue = tweetRepository.save(issueEntity);
            return tweetMapper.tweetToResponseTo(savedIssue);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "iss_content");
        }
    }

    public TweetResponseTo updateIssue(TweetRequestTo tweetRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        if (tweetRepository.findById(tweetRequestTo.getId()).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, tweetRequestTo.getId());
        }

        Optional<Editor> editor = editorRepository.findById(tweetRequestTo.getEditorId());

        if (editor.isEmpty()) {
            throw new EntityNotFoundException("Editor", tweetRequestTo.getEditorId());
        }
        if (tweetRequestTo.getCreated() == null) {
            tweetRequestTo.setCreated(new Date());
        }
        if (tweetRequestTo.getModified() == null) {
            tweetRequestTo.setModified(new Date());
        }

        Tweet tweet = tweetMapper.dtoToEntity(tweetRequestTo, editor.get());
        try {
            Tweet savedTweet = tweetRepository.save(tweet);
            return tweetMapper.tweetToResponseTo(savedTweet);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "tweet_content");
        }
    }

    public void deleteIssue(BigInteger id) throws EntityNotFoundException {
        if (tweetRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        tweetRepository.deleteById(id);
    }
}
