package com.example.publicator.service;

import com.example.publicator.dto.CreatorRequestTo;
import com.example.publicator.dto.CreatorResponseTo;
import com.example.publicator.exception.DuplicateException;
import com.example.publicator.exception.NotFoundException;
import com.example.publicator.mapper.CreatorListMapper;
import com.example.publicator.mapper.CreatorMapper;
import com.example.publicator.model.Creator;
import com.example.publicator.repository.CreatorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@Validated
public class CreatorService {

    @Autowired
    CreatorMapper creatorMapper;
    @Autowired
    CreatorRepository creatorRepository;
    @Autowired
    CreatorListMapper creatorListMapper;

    public CreatorResponseTo create(@Valid CreatorRequestTo creatorRequestTo){
        Creator creator = creatorMapper.creatorRequestToCreator(creatorRequestTo);
        if(creatorRepository.existsByLogin(creator.getLogin())){
            throw new DuplicateException("Login duplication", 403);
        }
        return creatorMapper.creatorToCreatorResponse(creatorRepository.save(creator));
    }
    public List<CreatorResponseTo> readAll(int pageInd, int numOfElem, String sortedBy, String direction){
        Pageable p;
        if(direction != null && direction.equals("asc")){
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).ascending());
        }
        else{
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).descending());
        }
        Page<Creator> res = creatorRepository.findAll(p);
        return creatorListMapper.toCreatorResponseList(res.toList());
    }

    public CreatorResponseTo read(@Min(0) int id) throws NotFoundException {
        if(creatorRepository.existsById(id)){
            CreatorResponseTo cr = creatorMapper.creatorToCreatorResponse(creatorRepository.getReferenceById(id));
            return cr;
        }
        else
            throw new NotFoundException("Creator not found", 404);
    }
    public CreatorResponseTo update(@Valid CreatorRequestTo creator,  @Min(0) int id){
        if(creatorRepository.existsById(id)){
            Creator cr =  creatorMapper.creatorRequestToCreator(creator);
            cr.setId(id);
            return creatorMapper.creatorToCreatorResponse(creatorRepository.save(cr));
        }
        else
            throw new NotFoundException("Creator not found", 404);
    }
    public boolean delete(@Min(0) int id){
        if(creatorRepository.existsById(id)){
            creatorRepository.deleteById(id);
            return true;
        }
        else
            throw new NotFoundException("Creator not found", 404);
    }


}
