package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Issue;
import by.bsuir.publisher.dto.responses.IssueResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = IssueResponseConverter.class)
public interface CollectionIssueResponseConverter {
    List<IssueResponseDto> toListDto(List<Issue> tags);
}
