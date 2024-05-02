package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.TweetRequestDto;
import by.bsuir.publisher.dto.responses.TweetResponseDto;

import java.util.List;

public interface TweetService extends BaseService<TweetRequestDto, TweetResponseDto> {
    List<TweetResponseDto> readAll();
}
