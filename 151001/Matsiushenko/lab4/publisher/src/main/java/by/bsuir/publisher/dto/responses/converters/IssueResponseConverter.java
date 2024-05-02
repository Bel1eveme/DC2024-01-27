package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Issue;
import by.bsuir.publisher.dto.responses.IssueResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueResponseConverter {
    @Mapping(source = "user.id", target = "userId")
    IssueResponseDto toDto(Issue issue);
}
