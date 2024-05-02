package com.example.publicator.service;

import com.example.publicator.dto.IssueRequestTo;
import com.example.publicator.dto.IssueResponseTo;
import com.example.publicator.exception.DuplicateException;
import com.example.publicator.exception.NotFoundException;
import com.example.publicator.mapper.IssueListMapper;
import com.example.publicator.mapper.IssueMapper;
import com.example.publicator.model.Issue;
import com.example.publicator.repository.CreatorRepository;
import com.example.publicator.repository.IssueRepository;
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
public class IssueService {
    @Autowired
    IssueMapper issueMapper;
    @Autowired
    IssueListMapper issueListMapper;
    @Autowired
    IssueRepository issueRepository;
    //IssueDao issueDao;
    @Autowired
    CreatorRepository creatorRepository;

    public IssueResponseTo create(@Valid IssueRequestTo issueRequestTo){
        Issue issue = issueMapper.issueRequestToIssue(issueRequestTo);
        if(issueRepository.existsByTitle(issue.getTitle())){
            throw new DuplicateException("Title duplication", 403);
        }
        if(issueRequestTo.getCreatorId() != 0){
            issue.setCreator(creatorRepository.findById(issueRequestTo.getCreatorId()).orElseThrow(() -> new NotFoundException("Creator not found", 404)));
        }
        return issueMapper.issueToIssueResponse(issueRepository.save(issue));
    }

    public List<IssueResponseTo> readAll(int pageInd, int numOfElem, String sortedBy, String direction) {
        Pageable p;
        if(direction != null && direction.equals("asc"))
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).ascending());
        else
            p = PageRequest.of(pageInd,numOfElem, Sort.by(sortedBy).descending());
        Page<Issue> res = issueRepository.findAll(p);
        return issueListMapper.toIssueResponseList(res.toList());
    }

    public IssueResponseTo read(@Min(0) int id) throws NotFoundException {
        if(issueRepository.existsById(id)){
            IssueResponseTo issue = issueMapper.issueToIssueResponse(issueRepository.getReferenceById(id));
            return issue;
        }
        else
            throw new NotFoundException("Issue not found", 404);
    }

    public IssueResponseTo update(@Valid IssueRequestTo issueRequestTo, @Min(0) int id) throws NotFoundException {
        if(issueRepository.existsById(id)){
            Issue issue = issueMapper.issueRequestToIssue(issueRequestTo);
            issue.setId(id);
            if(issueRequestTo.getCreatorId() != 0){
                issue.setCreator(creatorRepository.findById(issueRequestTo.getCreatorId()).orElseThrow(() -> new NotFoundException("Creator not found", 404)));
            }
            return issueMapper.issueToIssueResponse(issueRepository.save(issue));
        }
        else
            throw new NotFoundException("Issue not found", 404);
    }
    public boolean delete(@Min(0) int id) throws NotFoundException{
        if(issueRepository.existsById(id)){
            issueRepository.deleteById(id);
            return true;
        }
        else
            throw new NotFoundException("Issue not found", 404);
    }

}
