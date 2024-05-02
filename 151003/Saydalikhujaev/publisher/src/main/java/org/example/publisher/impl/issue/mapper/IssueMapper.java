package org.example.publisher.impl.issue.mapper;

import org.example.publisher.impl.creator.Creator;
import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.issue.dto.IssueRequestTo;
import org.example.publisher.impl.issue.dto.IssueResponseTo;

import java.util.List;

public interface IssueMapper {

    IssueRequestTo issueToRequestTo(Issue issue);

    List<IssueRequestTo> issueToRequestTo(Iterable<Issue> issues);

    Issue dtoToEntity(IssueRequestTo issueRequestTo, Creator creator);

    IssueResponseTo issueToResponseTo(Issue issue);

    List<IssueResponseTo> issueToResponseTo(Iterable<Issue> issues);
}
