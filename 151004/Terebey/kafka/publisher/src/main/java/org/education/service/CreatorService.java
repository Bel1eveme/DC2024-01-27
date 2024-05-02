package org.education.service;

import org.education.bean.Creator;
import org.education.bean.dto.CreatorResponseTo;
import org.education.exception.AlreadyExists;
import org.education.exception.NoSuchCreator;
import org.education.repository.CreatorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "creatorCache")
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CreatorService(CreatorRepository creatorRepository, ModelMapper modelMapper) {
        this.creatorRepository = creatorRepository;
        this.modelMapper = modelMapper;
    }

    @Cacheable(cacheNames = "creators")
    public List<CreatorResponseTo> getAll(){
        List<CreatorResponseTo> res = new ArrayList<>();
        for(Creator creator : creatorRepository.findAll()){
            res.add(modelMapper.map(creator, CreatorResponseTo.class));
        }
        return res;
    }

    @Cacheable(cacheNames = "creators", key = "#id", unless = "#result == null")
    public CreatorResponseTo getById(int id){
        return modelMapper.map(creatorRepository.getReferenceById(id), CreatorResponseTo.class);
    }

    @CacheEvict(cacheNames = "creators", allEntries = true)
    public CreatorResponseTo create(Creator creator){
        if(creatorRepository.existsCreatorByLogin(creator.getLogin())) throw new AlreadyExists("Creator with this login already exists");
        creatorRepository.save(creator);
        return modelMapper.map(creator, CreatorResponseTo.class);
    }

    @CacheEvict(cacheNames = "creators", allEntries = true)
    public CreatorResponseTo update(Creator creator){
        if(!creatorRepository.existsById(creator.getId())) throw new NoSuchCreator("There is no such creator with this id");
        creatorRepository.save(creator);
        return modelMapper.map(creator, CreatorResponseTo.class);
    }

    @Caching(evict = { @CacheEvict(cacheNames = "creators", key = "#id"),
            @CacheEvict(cacheNames = "creators", allEntries = true) })
    public void delete(int id){
        if(!creatorRepository.existsById(id)) throw new NoSuchCreator("There is no such creator with this id");
        creatorRepository.deleteById(id);
    }
}