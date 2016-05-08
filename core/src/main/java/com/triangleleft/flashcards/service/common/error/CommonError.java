package com.triangleleft.flashcards.service.common.error;

import com.google.auto.value.AutoValue;
import com.google.gson.stream.MalformedJsonException;

import com.triangleleft.flashcards.util.TextUtils;

import android.support.annotation.Nullable;

import java.io.IOException;

@AutoValue
public abstract class CommonError {
    private static final String INTERNAL_ERROR = "Internal error";

    public abstract ErrorType getType();

    public abstract String getMessage();

    public static CommonError create(ErrorType type, String message) {
        return new AutoValue_CommonError(type, message);
    }

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
        return new AutoValue_CommonError(type, message);
    }
}
