package com.bsuir.kirillpastukhou.serivces;

import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.request.TweetRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TweetResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface TweetService {
    @Validated(ValidationMarker.OnCreate.class)
    TweetResponseTo create(@Valid TweetRequestTo entity);

    List<TweetResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    TweetResponseTo update(@Valid TweetRequestTo entity);

    void delete(Long id);

    TweetResponseTo findTweetById(Long id);

    Optional<Tweet> findTweetByIdExt(Long id);
}
