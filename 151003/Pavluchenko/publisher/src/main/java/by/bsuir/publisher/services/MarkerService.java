package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.MarkerRequestDto;
import by.bsuir.publisher.dto.responses.MarkerResponseDto;

import java.util.List;

public interface MarkerService extends BaseService<MarkerRequestDto, MarkerResponseDto> {
    List<MarkerResponseDto> readAll();
}
