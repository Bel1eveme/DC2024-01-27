package by.bsuir.nastassiayankova.Storage.impl;

import by.bsuir.nastassiayankova.Entity.Tag;
import by.bsuir.nastassiayankova.Storage.InMemoryRepository;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagRepository implements InMemoryRepository<Tag> {
    private final Map<Long, Tag> TagMemory = new HashMap<>();

    @Override
    public Tag get(long id) {
        Tag tag = TagMemory.get(id);
        if (tag != null) {
            tag.setId(id);
        }
        return tag;
    }

    @Override
    public List<Tag> getAll() {
        List<Tag> tagList = new ArrayList<>();
        for (Long key : TagMemory.keySet()) {
            Tag tag = TagMemory.get(key);
            tag.setId(key);
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public Tag delete(long id) {
        return TagMemory.remove(id);
    }

    @Override
    public Tag insert(Tag insertObject) {
        TagMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Tag updatingValue) {
        return TagMemory.replace(updatingValue.getId(), TagMemory.get(updatingValue.getId()), updatingValue);
    }
}
