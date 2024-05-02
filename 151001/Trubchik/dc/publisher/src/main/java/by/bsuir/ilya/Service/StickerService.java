package by.bsuir.ilya.Service;


import by.bsuir.ilya.Entity.Sticker;
import by.bsuir.ilya.Entity.Sticker;
import by.bsuir.ilya.Entity.Sticker;
import by.bsuir.ilya.Entity.Sticker;


import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.redis.RedisStickerRepository;
import by.bsuir.ilya.repositories.StickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StickerService implements IService<StickerResponseTo, StickerRequestTo> {
    private final StickerRepository stickerRepository;

    @Autowired
    public StickerService(StickerRepository stickerRepository) {
        this.stickerRepository = stickerRepository;
    }

    @Autowired
    private RedisStickerRepository redisRepository;

    public List<StickerResponseTo> getAll() {
        List<Sticker> stickerList = redisRepository.getAll();;
        List<StickerResponseTo> resultList = new ArrayList<>();
        if(!stickerList.isEmpty())
        {
            for (int i = 0; i < stickerList.size(); i++) {
                resultList.add(StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerList.get(i)));
            }
        }else
        {
            stickerList = stickerRepository.findAll();
            for (int i = 0; i < stickerList.size(); i++) {
                resultList.add(StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerList.get(i)));
                redisRepository.add(stickerList.get(i));
            }
        }
        return resultList;
    }

    public StickerResponseTo update(StickerRequestTo updatingSticker) {
        Sticker sticker = StickerMapper.INSTANCE.stickerRequestToToSticker(updatingSticker);
        if (validateSticker(sticker)) {
            StickerResponseTo responseTo;
            Optional<Sticker> redisSticker = redisRepository.getById(sticker.getId());
            if(redisSticker.isPresent() && sticker.equals(redisSticker.get()))
            {
                return StickerMapper.INSTANCE.stickerToStickerResponseTo(redisSticker.get());
            }
            try {
                Sticker result = stickerRepository.save(sticker);
                redisRepository.update(result);
                responseTo = StickerMapper.INSTANCE.stickerToStickerResponseTo(result);
            }catch (Exception e)
            {
                e.getMessage();
                return new StickerResponseTo();
            }
            return responseTo;
        } else return new StickerResponseTo();
        //return responseTo;
    }

    public StickerResponseTo getById(long id) {
        Optional<Sticker> redisSticker = redisRepository.getById(id);
        Sticker result;
        if(redisSticker.isPresent())
        {
            return StickerMapper.INSTANCE.stickerToStickerResponseTo(redisSticker.get());
        }
        else {
            result = stickerRepository.findById(id);
            redisRepository.add(result);
        }
        return StickerMapper.INSTANCE.stickerToStickerResponseTo(result);
    }

    public boolean deleteById(long id) {
            if(stickerRepository.existsById(id)) {
                stickerRepository.deleteById(id);
                redisRepository.delete(id);
                return true;
            }
            return false;
    }

    public ResponseEntity<StickerResponseTo> add(StickerRequestTo stickerRequestTo) {
        Sticker sticker = StickerMapper.INSTANCE.stickerRequestToToSticker(stickerRequestTo);
        if (validateSticker(sticker)) {
            StickerResponseTo responseTo;
            try {
                Sticker result = stickerRepository.save(sticker);
                redisRepository.add(result);
                responseTo = StickerMapper.INSTANCE.stickerToStickerResponseTo(result);
            } catch (Exception e)
            {
                sticker.setId(sticker.getId()-1);
                return new ResponseEntity<>(StickerMapper.INSTANCE.stickerToStickerResponseTo(sticker), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(responseTo, HttpStatus.CREATED);
        } else return new ResponseEntity<>(StickerMapper.INSTANCE.stickerToStickerResponseTo(sticker), HttpStatus.FORBIDDEN);
    }

    private boolean validateSticker(Sticker sticker) {
        if(sticker.getName()!=null) {
            String name = sticker.getName();
            if (name.length() >= 2 && name.length() <= 32) return true;
        }
        return false;
    }
}
