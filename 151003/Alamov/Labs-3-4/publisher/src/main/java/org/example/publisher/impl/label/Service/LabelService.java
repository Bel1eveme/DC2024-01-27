package org.example.publisher.impl.label.Service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.note.Note;
import org.example.publisher.impl.label.Label;
import org.example.publisher.impl.label.LabelRepository;
import org.example.publisher.impl.label.dto.LabelRequestTo;
import org.example.publisher.impl.label.dto.LabelResponseTo;
import org.example.publisher.impl.label.mapper.Impl.LabelMapperImpl;
import org.example.publisher.impl.tweet.Tweet;
import org.example.publisher.impl.tweet.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "labelCache")
public class LabelService {

    private final LabelRepository labelRepository;
    private final TweetRepository tweetRepository;

    private final LabelMapperImpl labelMapper;
    private final String ENTITY_NAME = "label";


    @Cacheable(cacheNames = "labels")
    public List<LabelResponseTo> getLabels() {
        List<Label> labels = labelRepository.findAll();
        List<LabelResponseTo> labelsTos = new ArrayList<>();
        for (var label: labels) {
            labelsTos.add(labelMapper.labelToResponseTo(label));
        }
        return labelsTos;
    }

    @Cacheable(cacheNames = "labels", key = "#id", unless = "#result == null")
    public LabelResponseTo getLabelById(BigInteger id) throws EntityNotFoundException {
        Optional<Label> label = labelRepository.findById(id);
        if (label.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return labelMapper.labelToResponseTo(label.get());
    }

    @CacheEvict(cacheNames = "labels", allEntries = true)
    public LabelResponseTo saveLabel(LabelRequestTo label) throws DuplicateEntityException {
        List<Tweet> tweets = new ArrayList<>();
        if (label.getTweetIds() != null) {
            tweets = tweetRepository.findAllById(label.getTweetIds());
        }
        Label entity = labelMapper.dtoToEntity(label, tweets);
        try {
            Label savedLabel = labelRepository.save(entity);
            return labelMapper.labelToResponseTo(savedLabel);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }

    }

    @CacheEvict(cacheNames = "labels", allEntries = true)
    public LabelResponseTo updateLabel(LabelRequestTo labelRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Label> stickerEntity = labelRepository.findById(labelRequestTo.getId());
        if (stickerEntity.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, labelRequestTo.getId());
        }
        List<Tweet> tweets = new ArrayList<>();
        if (labelRequestTo.getTweetIds() != null) {
            tweets = tweetRepository.findAllById(labelRequestTo.getTweetIds());
        }
        try {
            Label label = labelRepository.save(labelMapper.dtoToEntity(labelRequestTo, tweets));
            return labelMapper.labelToResponseTo(label);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    @Caching(evict = { @CacheEvict(cacheNames = "labels", key = "#id"),
            @CacheEvict(cacheNames = "labels", allEntries = true) })
    public void deleteLabelById(BigInteger id) throws EntityNotFoundException {
        if (labelRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        labelRepository.deleteById(id);
    }

}
