package com.example.rv.impl.tag.Service;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.impl.note.Note;
import com.example.rv.impl.tag.Tag;
import com.example.rv.impl.tag.TagRepository;
import com.example.rv.impl.tag.dto.TagRequestTo;
import com.example.rv.impl.tag.dto.TagResponseTo;
import com.example.rv.impl.tag.mapper.Impl.TagMapperImpl;
import com.example.rv.impl.tweet.Tweet;
import com.example.rv.impl.tweet.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TweetRepository tweetRepository;

    private final TagMapperImpl tagMapper;
    private final String ENTITY_NAME = "tag";

    public List<TagResponseTo> getTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponseTo> tagsTos = new ArrayList<>();
        for (var tag: tags) {
            tagsTos.add(tagMapper.tagToResponseTo(tag));
        }
        return tagsTos;
    }


    public TagResponseTo getTagById(BigInteger id) throws EntityNotFoundException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return tagMapper.tagToResponseTo(tag.get());
    }


    public TagResponseTo saveTag(TagRequestTo tag) throws DuplicateEntityException {
        List<Tweet> tweets = new ArrayList<>();
        if (tag.getTweetIds() != null) {
            tweets = tweetRepository.findAllById(tag.getTweetIds());
        }
        Tag entity = tagMapper.dtoToEntity(tag, tweets);
        try {
            Tag savedTag = tagRepository.save(entity);
            return tagMapper.tagToResponseTo(savedTag);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }

    }

    public TagResponseTo updateTag(TagRequestTo tagRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Tag> stickerEntity = tagRepository.findById(tagRequestTo.getId());
        if (stickerEntity.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, tagRequestTo.getId());
        }
        List<Tweet> tweets = new ArrayList<>();
        if (tagRequestTo.getTweetIds() != null) {
            tweets = tweetRepository.findAllById(tagRequestTo.getTweetIds());
        }
        try {
            Tag tag = tagRepository.save(tagMapper.dtoToEntity(tagRequestTo, tweets));
            return tagMapper.tagToResponseTo(tag);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    public void deleteTagById(BigInteger id) throws EntityNotFoundException {
        if (tagRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        tagRepository.deleteById(id);
    }

}
