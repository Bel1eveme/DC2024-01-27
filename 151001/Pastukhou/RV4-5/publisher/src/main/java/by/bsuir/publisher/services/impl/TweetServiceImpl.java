package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Tweet;
import by.bsuir.publisher.dto.requests.TweetRequestDto;
import by.bsuir.publisher.dto.requests.converters.TweetRequestConverter;
import by.bsuir.publisher.dto.responses.TweetResponseDto;
import by.bsuir.publisher.dto.responses.converters.CollectionTweetResponseConverter;
import by.bsuir.publisher.dto.responses.converters.TweetResponseConverter;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.TweetRepository;
import by.bsuir.publisher.services.TweetService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(rollbackFor = {EntityExistsException.class, NoEntityExistsException.class})
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetRequestConverter tweetRequestConverter;
    private final TweetResponseConverter tweetResponseConverter;
    private final CollectionTweetResponseConverter collectionTweetResponseConverter;

    @Override
    @Validated
    public TweetResponseDto create(@Valid @NonNull TweetRequestDto dto) throws EntityExistsException {
        Optional<Tweet> tweet = dto.getId() == null ? Optional.empty() : tweetRepository.findById(dto.getId());
        if (tweet.isEmpty()) {
            return tweetResponseConverter.toDto(tweetRepository.save(tweetRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<TweetResponseDto> read(@NonNull Long id) {
        return tweetRepository.findById(id).flatMap(tweet -> Optional.of(
                tweetResponseConverter.toDto(tweet)));
    }

    @Override
    @Validated
    public TweetResponseDto update(@Valid @NonNull TweetRequestDto dto) throws NoEntityExistsException {
        Optional<Tweet> tweet = dto.getId() == null || tweetRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(tweetRequestConverter.fromDto(dto));
        return tweetResponseConverter.toDto(tweetRepository.save(tweet.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        tweetRepository.deleteById(tweet.map(Tweet::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return tweet.get().getId();
    }

    @Override
    public List<TweetResponseDto> readAll() {
        return collectionTweetResponseConverter.toListDto(tweetRepository.findAll());
    }
}
