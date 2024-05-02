package org.education.service;

import org.education.bean.dto.LabelResponseTo;
import org.education.bean.dto.IssueResponseTo;
import org.education.bean.Label;
import org.education.bean.Issue;
import org.education.exception.AlreadyExists;
import org.education.exception.IncorrectValuesException;
import org.education.exception.NoSuchIssue;
import org.education.repository.CreatorRepository;
import org.education.repository.IssueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "issueCache")
public class IssueService {

    final IssueRepository issueRepository;
    final CreatorRepository creatorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IssueService(IssueRepository issueRepository, CreatorRepository creatorRepository, ModelMapper modelMapper) {
        this.issueRepository = issueRepository;
        this.creatorRepository = creatorRepository;
        this.modelMapper = modelMapper;
    }


    @CacheEvict(cacheNames = "issues", allEntries = true)
    public IssueResponseTo create(Issue issue, int ownerId){
        if(issueRepository.existsIssueByTitle(issue.getTitle())) throw new AlreadyExists("Tweet with this title already exists");
        if(!creatorRepository.existsById(ownerId)) throw new IncorrectValuesException("There is no creator with this id");
        issue.setCreator(creatorRepository.getReferenceById(ownerId));
        issue.setCreated(LocalDateTime.now());
        issue.setModified(LocalDateTime.now());
        issueRepository.save(issue);

        return modelMapper.map(issue, IssueResponseTo.class);
    }

    @Cacheable(cacheNames = "issues")
    public List<IssueResponseTo> getAll(){
        List<IssueResponseTo> res = new ArrayList<>();
        for(Issue issue : issueRepository.findAll()){
            res.add(modelMapper.map(issue, IssueResponseTo.class));
        }
        return res;
    }

    public boolean existsWithId(int id){
        return issueRepository.existsById(id);
    }

    @Cacheable(cacheNames = "issues", key = "#id", unless = "#result == null")
    public IssueResponseTo getById(int id){

        return modelMapper.map(issueRepository.getReferenceById(id), IssueResponseTo.class);
    }

    @CacheEvict(cacheNames = "issues", allEntries = true)
    public IssueResponseTo update(Issue issue){
        if(!issueRepository.existsById(issue.getId())) throw new NoSuchIssue("There is no such issue with this id");
        Issue prevTweet = issueRepository.getReferenceById(issue.getId());
        issue.setCreated(prevTweet.getCreated());
        issue.setModified(LocalDateTime.now());
        issueRepository.save(issue);
        return modelMapper.map(issue, IssueResponseTo.class);
    }

    @Caching(evict = { @CacheEvict(cacheNames = "issues", key = "#id"),
            @CacheEvict(cacheNames = "issues", allEntries = true) })
    public void delete(int id){
        if(!issueRepository.existsById(id)) throw new NoSuchIssue("There is no such issue with this id");
        issueRepository.deleteById(id);
    }
}