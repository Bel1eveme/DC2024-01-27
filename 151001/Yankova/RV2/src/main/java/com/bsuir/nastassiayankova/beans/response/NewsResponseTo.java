package com.bsuir.nastassiayankova.beans.response;

import java.time.LocalDateTime;
import java.util.List;

public record NewsResponseTo(Long id, Long userId, String title, String content, LocalDateTime created,
                             LocalDateTime modified, List<MessageResponseTo> messageList) {
}
