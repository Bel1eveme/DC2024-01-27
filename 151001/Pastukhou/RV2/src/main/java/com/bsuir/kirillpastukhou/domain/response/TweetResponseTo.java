package com.bsuir.kirillpastukhou.domain.response;

import java.time.LocalDateTime;
import java.util.List;

public record TweetResponseTo(
        Long id,
        Long creatorId,
        String title,
        String content,
        LocalDateTime created,
        LocalDateTime modified, //   List<TagResponseTo> stickerList
        List<MessageResponseTo> messageList) {
}
