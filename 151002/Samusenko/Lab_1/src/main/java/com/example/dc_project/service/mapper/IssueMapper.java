package com.example.dc_project.service.mapper;

import com.example.dc_project.model.entity.Issue;
import com.example.dc_project.model.request.IssueRequestTo;
import com.example.dc_project.model.response.IssueResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    IssueResponseTo getResponse(Issue issue);
    List<IssueResponseTo> getListResponse(Iterable<Issue> issues);
    Issue getIssue(IssueRequestTo issueRequestTo);
}