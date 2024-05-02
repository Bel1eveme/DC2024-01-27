package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.issue.dto.IssueRequestTo;
import org.example.publisher.impl.issue.dto.IssueResponseTo;
import org.example.publisher.impl.issue.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IssueResponseTo> getIssues() {
        return issueService.getIssues();
    }

    @GetMapping("/{id}")
    public IssueResponseTo getIssueById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return issueService.getIssueById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseTo createIssue(@Valid @RequestBody IssueRequestTo issueRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        return issueService.saveIssue(issueRequestTo);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseTo updateIssue(@Valid @RequestBody IssueRequestTo issue) throws EntityNotFoundException, DuplicateEntityException {
        return issueService.updateIssue(issue);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable BigInteger id) throws EntityNotFoundException {
        issueService.deleteIssue(id);
    }
}
