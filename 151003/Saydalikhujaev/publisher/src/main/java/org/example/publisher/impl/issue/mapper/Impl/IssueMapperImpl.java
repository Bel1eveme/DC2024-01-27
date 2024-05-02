package org.example.publisher.impl.issue.mapper.Impl;

import org.example.publisher.impl.creator.Creator;
import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.issue.dto.IssueRequestTo;
import org.example.publisher.impl.issue.dto.IssueResponseTo;
import org.example.publisher.impl.issue.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IssueMapperImpl implements IssueMapper {
    @Override
    public IssueRequestTo issueToRequestTo(Issue issue) {
        return new IssueRequestTo(
                issue.getId(),
                issue.getCreator().getId(),
                issue.getTitle(),
                issue.getContent(),
                issue.getCreated(),
                issue.getModified()
        );
    }

    @Override
    public List<IssueRequestTo> issueToRequestTo(Iterable<Issue> issues) {
        return StreamSupport.stream(issues.spliterator(), false)
                .map(this::issueToRequestTo)
                .collect(Collectors.toList());
    }

    @Override
    public Issue dtoToEntity(IssueRequestTo issueRequestTo, Creator creator) {
        return new Issue(
                issueRequestTo.getId(),
                creator,
                issueRequestTo.getTitle(),
                issueRequestTo.getContent(),
                issueRequestTo.getModified(),
                issueRequestTo.getCreated());
    }

    @Override
    public IssueResponseTo issueToResponseTo(Issue issue) {
        return new IssueResponseTo(
                issue.getId(),
                issue.getCreator().getId(),
                issue.getTitle(),
                issue.getContent(),
                issue.getCreated(),
                issue.getModified()
        );
    }

    @Override
    public List<IssueResponseTo> issueToResponseTo(Iterable<Issue> issues) {
        return StreamSupport.stream(issues.spliterator(), false)
                .map(this::issueToResponseTo)
                .collect(Collectors.toList());
    }
}
