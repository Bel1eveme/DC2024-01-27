package org.example.publisher.impl.note.dto;

import java.math.BigInteger;

public record NoteResponseTo(
        BigInteger id,
        BigInteger newsId,
        String content
) {
}
