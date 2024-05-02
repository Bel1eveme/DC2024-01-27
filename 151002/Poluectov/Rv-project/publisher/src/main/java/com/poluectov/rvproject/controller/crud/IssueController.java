package com.poluectov.rvproject.controller.crud;

import com.poluectov.rvproject.dto.issue.IssueRequestTo;
import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.model.Issue;
import com.poluectov.rvproject.service.CommonRestService;
import com.poluectov.rvproject.service.IssueService;
import com.poluectov.rvproject.utils.modelassembler.IssueModelAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issues")
public class IssueController extends CommonRESTController<Issue, IssueRequestTo, IssueResponseTo> {
    public IssueController(IssueService service,
                           IssueModelAssembler assembler) {
        super(service, assembler::toModel);
    }
}