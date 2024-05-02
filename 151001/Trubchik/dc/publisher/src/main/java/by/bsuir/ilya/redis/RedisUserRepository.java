package by.bsuir.ilya.redis;

import by.bsuir.ilya.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisUserRepository {
    @Autowired
    private RedisTemplate template;
    private static final String HASH_KEY = "User";


    public User add(User user){
        try {
            template.opsForHash().put(HASH_KEY, user.getId(), user);
            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return user;
        }catch (Exception e){
            return null;
        }
    }

    public User update(User user){
        Optional<User> currentUser = getById(user.getId());
        if(currentUser.isPresent()){
            template.opsForHash().put(HASH_KEY, user.getId(), user);

            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return user;
        }else{
            return null;
        }
    }

    public void delete(long id){
        template.opsForHash().delete(HASH_KEY,id);
    }

    public List<User> getAll(){
        List<User> User = new ArrayList<>();
        List<Object> data = template.opsForHash().values(HASH_KEY);
        for(Object obj : data){
            User.add((User) obj);
        }
        return User;
    }

    public Optional<User> getById(long id){
        Object User = template.opsForHash().get(HASH_KEY,id);
        return Optional.ofNullable((User)User);
    }
}
