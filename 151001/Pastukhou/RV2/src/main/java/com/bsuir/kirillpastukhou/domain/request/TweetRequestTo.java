package com.bsuir.kirillpastukhou.domain.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;

public record TweetRequestTo(
        Long id,
        Long creatorId,
        @Size(min = 3, max = 32, groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
        String title,
        @Size(min = 3, max = 32, groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
        @Pattern(regexp="^.*[a-zA-Z]+.*$", groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
        String content) {
}
