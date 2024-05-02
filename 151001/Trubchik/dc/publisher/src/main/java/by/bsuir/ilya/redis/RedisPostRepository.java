package by.bsuir.ilya.redis;

import by.bsuir.ilya.Entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisPostRepository {
    @Autowired
    private RedisTemplate template;
    private static final String HASH_KEY = "Post";


    public Post add(Post post){
        try {
            template.opsForHash().put(HASH_KEY, post.getId(), post);
            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return post;
        }catch (Exception e){
            return null;
        }
    }

    public Post update(Post post){
        Optional<Post> currentPost = getById(post.getId());
        if(currentPost.isPresent()){
            template.opsForHash().put(HASH_KEY, post.getId(), post);

            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return post;
        }else{
            return null;
        }
    }

    public void delete(long id){
        template.opsForHash().delete(HASH_KEY,id);
    }

    public List<Post> getAll(){
        List<Post> Post = new ArrayList<>();
        List<Object> data = template.opsForHash().values(HASH_KEY);
        for(Object obj : data){
            Post.add((Post) obj);
        }
        return Post;
    }

    public Optional<Post> getById(long id){
        Object Post = template.opsForHash().get(HASH_KEY,id);
        return Optional.ofNullable((Post)Post);
    }
}
