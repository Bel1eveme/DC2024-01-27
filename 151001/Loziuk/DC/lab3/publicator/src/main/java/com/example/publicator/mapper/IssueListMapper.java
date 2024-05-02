package com.example.publicator.mapper;

import com.example.publicator.dto.IssueRequestTo;
import com.example.publicator.dto.IssueResponseTo;
import com.example.publicator.model.Issue;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = IssueMapper.class)
public interface IssueListMapper {
    List<Issue> toIssueList(List<IssueRequestTo> list);

    List<IssueResponseTo> toIssueResponseList(List<Issue> list);
}
