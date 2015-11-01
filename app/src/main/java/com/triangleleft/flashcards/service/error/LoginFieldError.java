package com.triangleleft.flashcards.service.error;

import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import android.support.annotation.Nullable;

public class LoginFieldError extends LoginError {
    private final LoginResponseModel.ErrorField field;

    public LoginFieldError(@Nullable String message,
                           @Nullable LoginResponseModel.ErrorField errorField) {
        super(message);
        this.field = errorField;
    }

    @Nullable
    public LoginResponseModel.ErrorField getField() {
        return field;
    }
}
