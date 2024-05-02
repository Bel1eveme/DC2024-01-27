package org.example.publisher.impl.creator.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.creator.Creator;
import org.example.publisher.impl.creator.CreatorRepository;
import org.example.publisher.impl.creator.dto.CreatorRequestTo;
import org.example.publisher.impl.creator.dto.CreatorResponseTo;
import org.example.publisher.impl.creator.mapper.Impl.CreatorMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "creatorsCache")
public class CreatorService {

    private final CreatorRepository creatorRepository;

    private final CreatorMapperImpl creatorMapper;
    private final String ENTITY_NAME = "creator";


    @Cacheable(cacheNames = "creators")
    public List<CreatorResponseTo> getCreators(){
        List<Creator> creators = creatorRepository.findAll();
        return creatorMapper.creatorToResponseTo(creators);
    }
    @Cacheable(cacheNames = "creators", key = "#id", unless = "#result == null")
    public CreatorResponseTo getCreatorById(BigInteger id) throws EntityNotFoundException{
        Optional<Creator> creator = creatorRepository.findById(id);
        if (creator.isEmpty()){
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return creatorMapper.creatorToResponseTo(creator.get());
    }

    @CacheEvict(cacheNames = "creators", allEntries = true)
    public CreatorResponseTo createCreator(CreatorRequestTo creator) throws DuplicateEntityException {
        try {
            Creator savedCreator = creatorRepository.save(creatorMapper.dtoToEntity(creator));
            return creatorMapper.creatorToResponseTo(savedCreator);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "login");
        }
    }

    @CacheEvict(cacheNames = "creators", allEntries = true)
    public CreatorResponseTo updateCreator(CreatorRequestTo creator) throws EntityNotFoundException {
        if (creatorRepository.findById(creator.getId()).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, creator.getId());
        }
        Creator savedCreator = creatorRepository.save(creatorMapper.dtoToEntity(creator));
        return creatorMapper.creatorToResponseTo(savedCreator);
    }

    @Caching(evict = { @CacheEvict(cacheNames = "creators", key = "#id"),
            @CacheEvict(cacheNames = "creators", allEntries = true) })
    public void deleteCreator(BigInteger id) throws EntityNotFoundException {
        if (creatorRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        creatorRepository.deleteById(id);
    }
}
