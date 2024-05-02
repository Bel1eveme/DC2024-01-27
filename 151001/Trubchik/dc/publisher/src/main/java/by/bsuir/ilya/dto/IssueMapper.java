package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IssueMapper {
    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );


    IssueRequestTo issueToIssueRequestTo(Issue issue);
    @Mapping(target = "userId", source = "user.id")
    IssueResponseTo issueToIssueResponseTo(Issue issue);

    Issue issueResponseToToIssue(IssueResponseTo responseTo);

    @Mapping(target = "user.id", source = "userId")
    Issue issueRequestToToIssue(IssueRequestTo requestTo);
}
