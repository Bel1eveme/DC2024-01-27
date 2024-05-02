package org.example.publisher.impl.note.dto;

import java.math.BigInteger;

public record NoteAddedResponseTo(
        BigInteger id,
        BigInteger newsId,
        String content,
        String status
) {
}