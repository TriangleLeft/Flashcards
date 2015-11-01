package com.triangleleft.flashcards.service.error;

import android.support.annotation.Nullable;

public class NetworkError extends CommonError {
    public NetworkError(@Nullable String message) {
        super(message);
    }
}
