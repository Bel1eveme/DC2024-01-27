package org.example.publisher.impl.note.dto;

import java.math.BigInteger;

public record NoteAddedResponseTo(
        BigInteger id,
        BigInteger issueId,
        String content,
        String status
) {
}