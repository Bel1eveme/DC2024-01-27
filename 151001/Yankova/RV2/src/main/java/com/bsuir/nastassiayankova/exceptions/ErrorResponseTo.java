package com.bsuir.nastassiayankova.exceptions;

public record ErrorResponseTo(
        String errorMessage,
        String errorCode) {
}
