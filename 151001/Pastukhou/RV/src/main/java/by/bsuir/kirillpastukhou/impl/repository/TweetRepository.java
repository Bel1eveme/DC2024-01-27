package by.bsuir.kirillpastukhou.impl.repository;

import by.bsuir.kirillpastukhou.api.InMemoryRepository;
import by.bsuir.kirillpastukhou.impl.bean.Tweet;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TweetRepository implements InMemoryRepository<Tweet> {


    private final Map<Long, Tweet> TweetMemory = new HashMap<>();

    @Override
    public Tweet get(long id) {
        Tweet tweet = TweetMemory.get(id);
        if (tweet != null) {
            tweet.setId(id);
        }
        return tweet;
    }

    @Override
    public List<Tweet> getAll() {
        List<Tweet> tweetList = new ArrayList<>();
        for (Long key : TweetMemory.keySet()) {
            Tweet tweet = TweetMemory.get(key);
            ;
            tweet.setId(key);
            tweetList.add(tweet);
        }
        return tweetList;
    }

    @Override
    public Tweet delete(long id) {
        return TweetMemory.remove(id);
    }

    @Override
    public Tweet insert(Tweet insertObject) {
        TweetMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Tweet updatingValue) {
        return TweetMemory.replace(updatingValue.getId(), TweetMemory.get(updatingValue.getId()), updatingValue);
    }

}
