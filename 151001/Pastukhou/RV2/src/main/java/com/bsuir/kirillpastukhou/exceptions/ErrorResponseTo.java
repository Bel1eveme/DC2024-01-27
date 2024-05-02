package com.bsuir.kirillpastukhou.exceptions;

public record ErrorResponseTo(
        String errorMessage,
        String errorCode) {
}
