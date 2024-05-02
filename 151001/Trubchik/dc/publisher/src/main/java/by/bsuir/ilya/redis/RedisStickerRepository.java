package by.bsuir.ilya.redis;

import by.bsuir.ilya.Entity.Sticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisStickerRepository {
    @Autowired
    private RedisTemplate template;
    private static final String HASH_KEY = "Sticker";


    public Sticker add(Sticker sticker){
        try {
            template.opsForHash().put(HASH_KEY, sticker.getId(), sticker);
            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return sticker;
        }catch (Exception e){
            return null;
        }
    }

    public Sticker update(Sticker sticker){
        Optional<Sticker> currentSticker = getById(sticker.getId());
        if(currentSticker.isPresent()){
            template.opsForHash().put(HASH_KEY, sticker.getId(), sticker);

            template.expire(HASH_KEY,10, TimeUnit.SECONDS);
            return sticker;
        }else{
            return null;
        }
    }

    public void delete(long id){
        template.opsForHash().delete(HASH_KEY,id);
    }

    public List<Sticker> getAll(){
        List<Sticker> Sticker = new ArrayList<>();
        List<Object> data = template.opsForHash().values(HASH_KEY);
        for(Object obj : data){
            Sticker.add((Sticker) obj);
        }
        return Sticker;
    }

    public Optional<Sticker> getById(long id){
        Object Sticker = template.opsForHash().get(HASH_KEY,id);
        return Optional.ofNullable((Sticker)Sticker);
    }
}
