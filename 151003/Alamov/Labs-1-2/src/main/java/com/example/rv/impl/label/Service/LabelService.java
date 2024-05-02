package com.example.rv.impl.label.Service;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.impl.label.Label;
import com.example.rv.impl.label.LabelRepository;
import com.example.rv.impl.label.dto.LabelRequestTo;
import com.example.rv.impl.label.dto.LabelResponseTo;
import com.example.rv.impl.label.mapper.Impl.LabelMapperImpl;
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
public class LabelService {

    private final LabelRepository labelRepository;
    private final TweetRepository tweetRepository;

    private final LabelMapperImpl labelMapper;
    private final String ENTITY_NAME = "label";

    public List<LabelResponseTo> getLabels() {
        List<Label> labels = labelRepository.findAll();
        List<LabelResponseTo> labelsTos = new ArrayList<>();
        for (var label: labels) {
            labelsTos.add(labelMapper.labelToResponseTo(label));
        }
        return labelsTos;
    }


    public LabelResponseTo getLabelById(BigInteger id) throws EntityNotFoundException {
        Optional<Label> label = labelRepository.findById(id);
        if (label.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return labelMapper.labelToResponseTo(label.get());
    }


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

    public void deleteLabelById(BigInteger id) throws EntityNotFoundException {
        if (labelRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        labelRepository.deleteById(id);
    }

}
