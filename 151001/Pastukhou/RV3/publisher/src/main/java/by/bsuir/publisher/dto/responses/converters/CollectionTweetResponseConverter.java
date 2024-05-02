package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Tweet;
import by.bsuir.publisher.dto.responses.TweetResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TweetResponseConverter.class)
public interface CollectionTweetResponseConverter {
    List<TweetResponseDto> toListDto(List<Tweet> tags);
}
