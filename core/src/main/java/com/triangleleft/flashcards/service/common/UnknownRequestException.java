package com.triangleleft.flashcards.service.common;

public class UnknownRequestException extends RuntimeException {
    public UnknownRequestException(String tag) {
        super("Unknown request: " + tag);
    }
}
