package by.bsuir.ilya.Controllers;

import by.bsuir.ilya.Service.IssueService;
import by.bsuir.ilya.dto.IssueRequestTo;
import by.bsuir.ilya.dto.IssueResponseTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @GetMapping("/issues")
    public ResponseEntity<List<IssueResponseTo>> getAllIssues() {
        List<IssueResponseTo> issueResponseToList = issueService.getAll();
        return new ResponseEntity<>(issueResponseToList, HttpStatus.OK);
    }

    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueResponseTo> getIssue(@PathVariable long id) {
        IssueResponseTo issueResponseTo = issueService.getById(id);
        return new ResponseEntity<>(issueResponseTo, issueResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/issues")
    public ResponseEntity<IssueResponseTo> createIssue(@RequestBody IssueRequestTo issueTo) {
        return issueService.add(issueTo);
    }

    @DeleteMapping("/issues/{id}")
    public ResponseEntity<IssueResponseTo> deleteIssue(@PathVariable long id) {
        return new ResponseEntity<>(null,issueService.deleteById(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/issues")
    public ResponseEntity<IssueResponseTo> updateIssue(@RequestBody IssueRequestTo issueRequestTo) {
        IssueResponseTo issueResponseTo = issueService.update(issueRequestTo);
        return new ResponseEntity<>(issueResponseTo, issueResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
