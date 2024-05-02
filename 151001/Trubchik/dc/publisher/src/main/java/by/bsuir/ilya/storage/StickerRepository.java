package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Sticker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class StickerRepository implements InMemoryRepository<Sticker> {
    private Map<Long, Sticker> stickerMemory = new ConcurrentHashMap<>();
    AtomicLong lastId = new AtomicLong();

    @Override
    public Sticker findById(long id) {
        Sticker sticker = stickerMemory.get(id);
        return sticker;
    }

    @Override
    public List<Sticker> findAll() {
        List<Sticker> stickerList = new ArrayList<>();
        for (Long key : stickerMemory.keySet()) {
            Sticker sticker = stickerMemory.get(key);
            ;
            stickerList.add(sticker);
        }
        return stickerList;
    }

    @Override
    public Sticker deleteById(long id) {
        Sticker result = stickerMemory.remove(id);
        return result;
    }

    @Override
    public boolean deleteAll() {
        stickerMemory.clear();
        return true;
    }

    @Override
    public Sticker insert(Sticker insertObject) {
        long id = lastId.getAndIncrement();
        insertObject.setId(id);
        stickerMemory.put(id, insertObject);
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Sticker updatingSticker) {
        boolean status = stickerMemory.replace(id, stickerMemory.get(id), updatingSticker);
        return status;
    }

    @Override
    public boolean update(Sticker updatingValue) {
        boolean status = stickerMemory.replace(updatingValue.getId(), stickerMemory.get(updatingValue.getId()), updatingValue);
        return status;
    }


}
