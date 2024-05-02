package com.example.publicator.service;

import com.example.publicator.dto.TagRequestTo;
import com.example.publicator.dto.TagResponseTo;
import com.example.publicator.exception.NotFoundException;
import com.example.publicator.mapper.TagListMapper;
import com.example.publicator.mapper.TagMapper;
import com.example.publicator.model.Tag;
import com.example.publicator.repository.TagRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class TagService {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    TagListMapper tagListMapper;
    @Autowired
    //TagDao tagDao;
    TagRepository tagRepository;

    public TagResponseTo create(@Valid TagRequestTo tagRequestTo){
        return tagMapper.tagToTagResponse(tagRepository.save(tagMapper.tagRequestToTag(tagRequestTo)));
    }

    public List<TagResponseTo> readAll(int pageInd, int numOfElem, String sortedBy, String direction){
        Pageable p;
        if(direction != null && direction.equals("asc"))
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).ascending());
        else
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).descending());
        Page<Tag> res = tagRepository.findAll(p);
        return tagListMapper.toTagResponseList(res.toList());
    }

    public TagResponseTo read(@Min(0) int id) throws NotFoundException{
        if(tagRepository.existsById(id)){
            TagResponseTo tag = tagMapper.tagToTagResponse(tagRepository.getReferenceById(id));
            return tag;
        }
        else
            throw new NotFoundException("Tag not found",404);
    }

    public TagResponseTo update(@Valid TagRequestTo tagRequestTo, @Min(0) int id) throws NotFoundException{
        if(tagRepository.existsById(id)){
            Tag tag = tagMapper.tagRequestToTag(tagRequestTo);
            tag.setId(id);
            return tagMapper.tagToTagResponse(tagRepository.save(tag));
        }
        else
            throw new NotFoundException("Tag not found",404);
    }

    public boolean delete(@Min(0) int id) throws NotFoundException{
        if(tagRepository.existsById(id)){
            tagRepository.deleteById(id);
            return true;
        }
        else
            throw new NotFoundException("Tag not found",404);
    }



}
