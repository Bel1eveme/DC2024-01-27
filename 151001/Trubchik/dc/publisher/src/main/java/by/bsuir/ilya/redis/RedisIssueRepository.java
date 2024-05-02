package by.bsuir.ilya.redis;

import by.bsuir.ilya.Entity.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisIssueRepository {
    @Autowired
    private RedisTemplate template;
    private static final String HASH_KEY = "Issue";


    public Issue add(Issue issue){
        try {
            template.opsForHash().put(HASH_KEY, issue.getId(), issue);
            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return issue;
        }catch (Exception e){
            return null;
        }
    }

    public Issue update(Issue issue){
        Optional<Issue> currentIssue = getById(issue.getId());
        if(currentIssue.isPresent()){
            template.opsForHash().put(HASH_KEY, issue.getId(), issue);

            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return issue;
        }else{
            return null;
        }
    }

    public void delete(long id){
        template.opsForHash().delete(HASH_KEY,id);
    }

    public List<Issue> getAll(){
        List<Issue> Issue = new ArrayList<>();
        List<Object> data = template.opsForHash().values(HASH_KEY);
        for(Object obj : data){
            Issue.add((Issue) obj);
        }
        return Issue;
    }

    public Optional<Issue> getById(long id){
        Object Issue = template.opsForHash().get(HASH_KEY,id);
        return Optional.ofNullable((Issue)Issue);
    }
}
