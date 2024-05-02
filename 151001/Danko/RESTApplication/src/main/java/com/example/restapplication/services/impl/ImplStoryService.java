package com.example.restapplication.services.impl;

import com.example.restapplication.dto.StoryRequestTo;
import com.example.restapplication.dto.StoryResponseTo;
import com.example.restapplication.entites.Story;
import com.example.restapplication.exceptions.DeleteException;
import com.example.restapplication.exceptions.DuplicationException;
import com.example.restapplication.exceptions.NotFoundException;
import com.example.restapplication.exceptions.UpdateException;
import com.example.restapplication.mappers.StoryListMapper;
import com.example.restapplication.mappers.StoryMapper;
import com.example.restapplication.repository.StoryRepository;
import com.example.restapplication.repository.UserRepository;
import com.example.restapplication.services.StoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@CacheConfig(cacheNames = "storysCache")
public class ImplStoryService implements StoryService {

    @Autowired
    StoryMapper storyMapper;

    @Autowired
    StoryRepository storyDAO;

    @Autowired
    StoryListMapper storyListMapper;

    @Autowired
    UserRepository userDAO;
    @Override
    @Cacheable(value = "storys", key = "#id", unless = "#result == null")
    public StoryResponseTo getById(Long id) throws NotFoundException {
        Optional<Story> story = storyDAO.findById(id);
        return story.map(value -> storyMapper.toStoryResponse(value)).orElseThrow(() -> new NotFoundException("Story not found", 40004L));
    }

    @Override
    @Cacheable(cacheNames = "storys")
    public List<StoryResponseTo> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<Story> stories = storyDAO.findAll(pageable);
        return storyListMapper.toStoryResponseList(stories.toList());
    }


    @Override
    @CacheEvict(cacheNames = "storys", allEntries = true)
    public StoryResponseTo save(@Valid StoryRequestTo requestTo) {

        Story storyToSave = storyMapper.toStory(requestTo);
        if (storyDAO.existsByTitle(storyToSave.getTitle())) {
            throw new DuplicationException("Title duplication", 40005L);
        }
        if (requestTo.getUserId() != null) {
            storyToSave.setUser(userDAO.findById(requestTo.getUserId()).orElseThrow(() -> new NotFoundException("User not found!", 40004L)));
        }
        return storyMapper.toStoryResponse(storyDAO.save(storyToSave));
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "storys", key = "#id"),
            @CacheEvict(cacheNames = "storys", allEntries = true) })
    public void delete(Long id) throws DeleteException {
        if (!storyDAO.existsById(id)) {
            throw new DeleteException("Story not found!", 40004L);
        } else {
            storyDAO.deleteById(id);
        }
    }

    @Override
    @CacheEvict(cacheNames = "storys", allEntries = true)
    public StoryResponseTo update(@Valid StoryRequestTo requestTo) throws UpdateException {
        Story storyToUpdate = storyMapper.toStory(requestTo);
        if (!storyDAO.existsById(requestTo.getId())) {
            throw new UpdateException("Story not found!", 40004L);
        } else {
            if (requestTo.getUserId() != null) {
                storyToUpdate.setUser(userDAO.findById(requestTo.getUserId()).orElseThrow(() -> new NotFoundException("User not found!", 40004L)));
            }
            return storyMapper.toStoryResponse(storyDAO.save(storyToUpdate));
        }
    }


    @Override
    public List<StoryResponseTo> getByData(List<String> tagName, List<Long> tagId, String userLogin, String title, String content) {
        return storyListMapper.toStoryResponseList(storyDAO.findStoriesByParams(tagName, tagId, userLogin, title, content));
    }
}
