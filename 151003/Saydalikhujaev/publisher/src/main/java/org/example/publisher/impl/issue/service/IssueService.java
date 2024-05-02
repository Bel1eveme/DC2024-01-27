package org.example.publisher.impl.issue.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.creator.Creator;
import org.example.publisher.impl.creator.CreatorRepository;
import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.issue.IssueRepository;
import org.example.publisher.impl.issue.dto.IssueRequestTo;
import org.example.publisher.impl.issue.dto.IssueResponseTo;
import org.example.publisher.impl.issue.mapper.Impl.IssueMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final CreatorRepository creatorRepository;
    private final IssueMapperImpl issueMapper;

    private final String ENTITY_NAME = "issue";


    public List<IssueResponseTo> getIssues() {
        List<Issue> issues = issueRepository.findAll();
        List<IssueResponseTo> issueResponseTos = new ArrayList<>();

        for (var item : issues) {
            issueResponseTos.add(issueMapper.issueToResponseTo(item));
        }
        return issueResponseTos;
    }


    public IssueResponseTo getIssueById(BigInteger id) throws EntityNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return issueMapper.issueToResponseTo(issue.get());
    }

    public IssueResponseTo saveIssue(IssueRequestTo issueRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Creator> creator = creatorRepository.findById(issueRequestTo.getCreatorId());
        if (creator.isEmpty()) {
            throw new EntityNotFoundException("Creator", issueRequestTo.getCreatorId());
        }

        if (issueRequestTo.getCreated() == null) {
            issueRequestTo.setCreated(new Date());
        }
        if (issueRequestTo.getModified() == null) {
            issueRequestTo.setModified(new Date());
        }
        if (isNumeric(issueRequestTo.getContent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "iss_content should be a string, not a number.");
        }
        Issue issueEntity = issueMapper.dtoToEntity(issueRequestTo, creator.get());

        try {
            Issue savedIssue = issueRepository.save(issueEntity);
            return issueMapper.issueToResponseTo(savedIssue);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "iss_content");
        }
    }

    public IssueResponseTo updateIssue(IssueRequestTo issueRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        if (issueRepository.findById(issueRequestTo.getId()).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, issueRequestTo.getId());
        }

        Optional<Creator> creator = creatorRepository.findById(issueRequestTo.getCreatorId());

        if (creator.isEmpty()) {
            throw new EntityNotFoundException("Creator", issueRequestTo.getCreatorId());
        }
        if (issueRequestTo.getCreated() == null) {
            issueRequestTo.setCreated(new Date());
        }
        if (issueRequestTo.getModified() == null) {
            issueRequestTo.setModified(new Date());
        }

        Issue issue = issueMapper.dtoToEntity(issueRequestTo, creator.get());
        try {
            Issue savedIssue = issueRepository.save(issue);
            return issueMapper.issueToResponseTo(savedIssue);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "issue_content");
        }
    }

    public void deleteIssue(BigInteger id) throws EntityNotFoundException {
        if (issueRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        issueRepository.deleteById(id);
    }
}
