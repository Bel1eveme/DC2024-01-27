package org.education.service;

import org.education.bean.Creator;
import org.education.bean.dto.CreatorResponseTo;
import org.education.bean.dto.LabelResponseTo;
import org.education.bean.Label;
import org.education.exception.NoSuchLabel;
import org.education.repository.LabelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "labelCache")
public class LabelService {

    private final LabelRepository labelRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LabelService(LabelRepository markerRepository, ModelMapper modelMapper) {
        this.labelRepository = markerRepository;
        this.modelMapper = modelMapper;
    }

    @Cacheable(cacheNames = "labels")
    public List<LabelResponseTo> getAll(){
        List<LabelResponseTo> res = new ArrayList<>();
        for(Label label : labelRepository.findAll()){
            res.add(modelMapper.map(label, LabelResponseTo.class));
        }
        return res;
    }
    @Cacheable(cacheNames = "labels", key = "#id", unless = "#result == null")
    public LabelResponseTo getById(int id){
        return modelMapper.map(labelRepository.getReferenceById(id), LabelResponseTo.class);
    }

    @CacheEvict(cacheNames = "labels", allEntries = true)
    public LabelResponseTo create(Label label){
        labelRepository.save(label);
        return modelMapper.map(label, LabelResponseTo.class);
    }

    @CacheEvict(cacheNames = "labels", allEntries = true)
    public LabelResponseTo update(Label label){
        if(!labelRepository.existsById(label.getId())) throw new NoSuchLabel("There is no such label with this id");
        labelRepository.save(label);
        return modelMapper.map(label, LabelResponseTo.class);
    }

    @Caching(evict = { @CacheEvict(cacheNames = "labels", key = "#id"),
            @CacheEvict(cacheNames = "labels", allEntries = true) })
    public void delete(int id){
        if(!labelRepository.existsById(id)) throw new NoSuchLabel("There is no such label with this id");
        labelRepository.deleteById(id);
    }
}