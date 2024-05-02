package com.example.publicator.controller;

import com.example.publicator.dto.IssueRequestTo;
import com.example.publicator.dto.IssueResponseTo;
import com.example.publicator.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class IssueController {
    @Autowired
    IssueService issueService;

    @GetMapping(value = "issues", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> readAll(
            @RequestParam (required = false, defaultValue = "0") Integer pageInd,
            @RequestParam (required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam (required = false, defaultValue = "id") String sortedBy,
            @RequestParam (required = false, defaultValue = "desc") String direction)
    {
        List<IssueResponseTo> list = issueService.readAll(pageInd, numOfElem, sortedBy, direction);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "issues/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") int id) {
        IssueResponseTo issue = issueService.read(id);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PostMapping(value = "issues")
    public ResponseEntity<?> create(@RequestBody IssueRequestTo issueRequestTo) {
        IssueResponseTo issueResponseTo = issueService.create(issueRequestTo);
        return new ResponseEntity<>(issueResponseTo, HttpStatus.CREATED);
    }

    @PutMapping(value = "issues")
    public ResponseEntity<?> update(@RequestBody IssueRequestTo issueRequestTo) {
        IssueResponseTo issueResponseTo = issueService.update(issueRequestTo, issueRequestTo.getId());
        return new ResponseEntity<>(issueResponseTo, HttpStatus.OK);
    }

    @DeleteMapping(value = "issues/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        boolean res = issueService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
