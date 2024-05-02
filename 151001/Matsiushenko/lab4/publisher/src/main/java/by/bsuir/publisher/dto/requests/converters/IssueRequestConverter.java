package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Issue;
import by.bsuir.publisher.dto.requests.IssueRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueRequestConverter {
    @Mapping(source = "userId", target = "user.id")
    Issue fromDto(IssueRequestDto issue);
}
