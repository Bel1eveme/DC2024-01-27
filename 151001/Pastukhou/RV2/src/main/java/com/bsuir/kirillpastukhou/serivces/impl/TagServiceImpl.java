package com.bsuir.kirillpastukhou.serivces.impl;

import com.bsuir.kirillpastukhou.domain.entity.Tag;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.mapper.TagMapper;
import com.bsuir.kirillpastukhou.domain.request.TagRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TagResponseTo;
import com.bsuir.kirillpastukhou.exceptions.NoSuchMessageException;
import com.bsuir.kirillpastukhou.exceptions.NoSuchTagException;
import com.bsuir.kirillpastukhou.serivces.TagService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.bsuir.kirillpastukhou.domain.mapper.TagListMapper;
import com.bsuir.kirillpastukhou.repositories.TagRepository;

import java.util.List;

@Service
@Transactional
@Validated
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagListMapper tagListMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, TagListMapper tagListMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.tagListMapper = tagListMapper;
    }

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public TagResponseTo create(@Valid TagRequestTo entity) {
        return tagMapper.toTagResponseTo(tagRepository.save(tagMapper.toTag(entity)));
    }

    @Override
    public List<TagResponseTo> read() {
        return tagListMapper.toTagResponseToList(tagRepository.findAll());
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public TagResponseTo update(@Valid TagRequestTo entity) {
        if (tagRepository.existsById(entity.id())) {
            Tag tag = tagMapper.toTag(entity);
            Tag tagRef = tagRepository.getReferenceById(tag.getId());
            tag.setTweetTagList(tagRef.getTweetTagList());
            return tagMapper.toTagResponseTo(tagRepository.save(tag));
        } else {
            throw new NoSuchMessageException(entity.id());
        }
    }

    @Override
    public void delete(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else {
            throw new NoSuchMessageException(id);
        }
    }

    @Override
    public TagResponseTo findTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new NoSuchTagException(id));
        return tagMapper.toTagResponseTo(tag);
    }
}
