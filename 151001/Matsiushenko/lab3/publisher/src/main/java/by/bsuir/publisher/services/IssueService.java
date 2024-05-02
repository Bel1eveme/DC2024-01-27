package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.IssueRequestDto;
import by.bsuir.publisher.dto.responses.IssueResponseDto;

import java.util.List;

public interface IssueService extends BaseService<IssueRequestDto, IssueResponseDto> {
    List<IssueResponseDto> readAll();
}
