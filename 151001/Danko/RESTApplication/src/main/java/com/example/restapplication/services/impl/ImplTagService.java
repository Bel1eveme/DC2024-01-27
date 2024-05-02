package com.example.restapplication.services.impl;

import com.example.restapplication.dto.TagRequestTo;
import com.example.restapplication.dto.TagResponseTo;
import com.example.restapplication.entites.Tag;
import com.example.restapplication.exceptions.DeleteException;
import com.example.restapplication.exceptions.NotFoundException;
import com.example.restapplication.exceptions.UpdateException;
import com.example.restapplication.mappers.TagListMapper;
import com.example.restapplication.mappers.TagMapper;
import com.example.restapplication.repository.TagRepository;
import com.example.restapplication.services.TagService;
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
@CacheConfig(cacheNames = "tagsCache")
public class ImplTagService implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    TagRepository tagDAO;

    @Autowired
    TagListMapper tagListMapper;
    @Override
    @Cacheable(cacheNames = "tags", key = "#id", unless = "#result == null")
    public TagResponseTo getById(Long id) throws NotFoundException {
        Optional<Tag> tag = tagDAO.findById(id);
        return tag.map(value -> tagMapper.toTagResponse(value)).orElseThrow(() -> new NotFoundException("Tag not found", 40004L));
    }

    @Override
    @Cacheable(cacheNames = "tags")
    public List<TagResponseTo> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<Tag> tag = tagDAO.findAll(pageable);
        return tagListMapper.toTagResponseList(tag.toList());
    }

    @Override
    @CacheEvict(cacheNames = "tags", allEntries = true)
    public TagResponseTo save(@Valid TagRequestTo requestTo) {
        Tag tagToSave = tagMapper.toTag(requestTo);
        return tagMapper.toTagResponse(tagDAO.save(tagToSave));
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "tags", key = "#id"),
            @CacheEvict(cacheNames = "tags", allEntries = true) })
    public void delete(Long id) throws DeleteException {
        if (!tagDAO.existsById(id)) {
            throw new DeleteException("Tag not found!", 40004L);
        } else {
            tagDAO.deleteById(id);
        }
    }

    @Override
    @CacheEvict(cacheNames = "tags", allEntries = true)
    public TagResponseTo update(@Valid TagRequestTo requestTo) throws UpdateException {
        Tag tagToUpdate = tagMapper.toTag(requestTo);
        if(!tagDAO.existsById(tagToUpdate.getId())) {
            throw new UpdateException("Tag not found!", 40004L);
        } else {
            return tagMapper.toTagResponse(tagDAO.save(tagToUpdate));
        }
    }

    @Override
    public List<TagResponseTo> getByStoryId(Long storyId) throws NotFoundException {
        List<Tag> tags = tagDAO.findTagsByStoryId(storyId);
        return tagListMapper.toTagResponseList(tags);
    }
}
