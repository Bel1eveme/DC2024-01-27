package by.bsuir.nastassiayankova.Service.impl;


import by.bsuir.nastassiayankova.Entity.Tag;
import by.bsuir.nastassiayankova.Service.IService;
import by.bsuir.nastassiayankova.Dto.*;
import by.bsuir.nastassiayankova.Dto.impl.TagRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.TagResponseTo;
import by.bsuir.nastassiayankova.Storage.impl.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagService implements IService<TagResponseTo, TagRequestTo> {
    @Autowired
    private TagRepository tagRepository;

    public List<TagResponseTo> getAll() {
        List<Tag> tagList = tagRepository.getAll();
        List<TagResponseTo> resultList = new ArrayList<>();
        for (Tag tag : tagList) {
            resultList.add(TagMapper.INSTANCE.TagToTagResponseTo(tag));
        }
        return resultList;
    }

    public TagResponseTo update(TagRequestTo updatingTag) {
        Tag tag = TagMapper.INSTANCE.TagRequestToToTag(updatingTag);
        if (validateTag(tag)) {
            boolean result = tagRepository.update(tag);
            return result ? TagMapper.INSTANCE.TagToTagResponseTo(tag) : null;
        } else return new TagResponseTo();
        //return responseTo;
    }

    public TagResponseTo get(long id) {
        return TagMapper.INSTANCE.TagToTagResponseTo(tagRepository.get(id));
    }

    public TagResponseTo delete(long id) {
        return TagMapper.INSTANCE.TagToTagResponseTo(tagRepository.delete(id));
    }

    public TagResponseTo add(TagRequestTo tagRequestTo) {
        Tag tag = TagMapper.INSTANCE.TagRequestToToTag(tagRequestTo);
        return TagMapper.INSTANCE.TagToTagResponseTo(tagRepository.insert(tag));
    }

    private boolean validateTag(Tag tag) {
        String content = tag.getName();
        return content.length() >= 2 && content.length() <= 2048;
    }
}
