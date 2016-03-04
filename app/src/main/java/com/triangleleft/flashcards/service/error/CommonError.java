package com.triangleleft.flashcards.service.error;

import com.google.gson.stream.MalformedJsonException;

import com.triangleleft.flashcards.util.TextUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

public class CommonError {
    private static final String INTERNAL_ERROR = "Internal error";

    private final String message;
    private final ErrorType type;

    public CommonError(@NonNull ErrorType type, @Nullable String message) {
        this.message = message;
        this.type = type;
    }

    @NonNull
    public ErrorType getType() {
        return type;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '@' + type + '@' + message + '@' + Integer.toHexString(hashCode());
    }

    @NonNull
    public static CommonError fromThrowable(@Nullable Throwable throwable) {
        String message;
        ErrorType type;
        if (throwable == null) {
            type = ErrorType.INTERNAL;
            message = INTERNAL_ERROR;
        } else {
            message = TextUtils.isEmpty(throwable.getMessage()) ? throwable.getClass().getSimpleName() :
                    throwable.getMessage();
            if (throwable instanceof MalformedJsonException) {
                type = ErrorType.CONVERSION;
            } else if (throwable instanceof IOException) {
                // Treat IOExceptions as network
                type = ErrorType.NETWORK;
            } else {
                type = ErrorType.INTERNAL;
            }
        }
        return new CommonError(type, message);
    }
}
