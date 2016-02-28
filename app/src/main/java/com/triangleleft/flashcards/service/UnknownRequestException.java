package com.triangleleft.flashcards.service;

public class UnknownRequestException extends RuntimeException {
    public UnknownRequestException(String tag) {
        super("Unknown request: " + tag);
    }
}
