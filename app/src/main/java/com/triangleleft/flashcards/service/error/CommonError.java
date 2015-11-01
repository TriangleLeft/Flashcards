package com.triangleleft.flashcards.service.error;

import android.support.annotation.Nullable;

public class CommonError {
    private final String message;

    public CommonError(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
