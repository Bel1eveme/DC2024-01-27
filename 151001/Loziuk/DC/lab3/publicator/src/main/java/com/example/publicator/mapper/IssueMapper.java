package com.example.publicator.mapper;

import com.example.publicator.dto.IssueRequestTo;
import com.example.publicator.dto.IssueResponseTo;
import com.example.publicator.model.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    Issue issueRequestToIssue(IssueRequestTo issueRequestTo);
    @Mapping(target = "creatorId", source = "issue.creator.id")

    IssueResponseTo issueToIssueResponse(Issue issue);
}
