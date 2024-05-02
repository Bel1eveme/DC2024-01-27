package by.bsuir.kirillpastukhou.impl.service;

import by.bsuir.kirillpastukhou.api.Service;
import by.bsuir.kirillpastukhou.impl.bean.Tweet;
import by.bsuir.kirillpastukhou.api.TweetMapper;
import by.bsuir.kirillpastukhou.impl.dto.TweetRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.TweetResponseTo;
import by.bsuir.kirillpastukhou.impl.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetService implements Service<TweetResponseTo, TweetRequestTo> {
    @Autowired
    private TweetRepository tweetRepository;

    public TweetService() {

    }

    public List<TweetResponseTo> getAll() {
        List<Tweet> tweetList = tweetRepository.getAll();
        List<TweetResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < tweetList.size(); i++) {
            resultList.add(TweetMapper.INSTANCE.TweetToTweetResponseTo(tweetList.get(i)));
        }
        return resultList;
    }

    public TweetResponseTo update(TweetRequestTo updatingTweet) {
        Tweet tweet = TweetMapper.INSTANCE.TweetRequestToToTweet(updatingTweet);
        if (validateTweet(tweet)) {
            boolean result = tweetRepository.update(tweet);
            TweetResponseTo responseTo = result ? TweetMapper.INSTANCE.TweetToTweetResponseTo(tweet) : null;
            return responseTo;
        } else return new TweetResponseTo();
        //return responseTo;
    }

    public TweetResponseTo get(long id) {
        return TweetMapper.INSTANCE.TweetToTweetResponseTo(tweetRepository.get(id));
    }

    public TweetResponseTo delete(long id) {
        return TweetMapper.INSTANCE.TweetToTweetResponseTo(tweetRepository.delete(id));
    }

    public TweetResponseTo add(TweetRequestTo tweetRequestTo) {
        Tweet tweet = TweetMapper.INSTANCE.TweetRequestToToTweet(tweetRequestTo);
        return TweetMapper.INSTANCE.TweetToTweetResponseTo(tweetRepository.insert(tweet));
    }

    private boolean validateTweet(Tweet tweet) {
        String title = tweet.getTitle();
        String content = tweet.getContent();

        if ((content.length() >= 4 && content.length() <= 2048) && (title.length() >= 2 && title.length() <= 64)) {
            return true;
        }
        return false;
    }
}
