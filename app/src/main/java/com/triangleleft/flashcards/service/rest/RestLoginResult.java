package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.LoginStatus;
import com.triangleleft.flashcards.service.error.CommonError;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RestLoginResult implements ILoginResult {

    private final CommonError error;
    private final LoginStatus status;

    public RestLoginResult(@NonNull CommonError error) {
        this(null, error);
    }

    public RestLoginResult(@NonNull LoginStatus status) {
        this(status, null);
    }

    public RestLoginResult(@Nullable LoginStatus status, @Nullable CommonError error) {
        this.error = error;
        this.status = status;
    }

    @Override
    public boolean isSuccess() {
        return getError() == null;
    }

    @Override
    public CommonError getError() {
        return error;
    }

    @Override
    public LoginStatus getResult() {
        return status;
    }
}
