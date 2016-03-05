package com.triangleleft.flashcards.service.provider;

public class UnknownRequestException extends RuntimeException {
    public UnknownRequestException(String tag) {
        super("Unknown request: " + tag);
    }
}
