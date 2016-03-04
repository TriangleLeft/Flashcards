package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.LoginStatus;

import android.support.annotation.Nullable;

public class SimpleLoginResult implements ILoginResult {

    private final LoginStatus status;

    public SimpleLoginResult(@Nullable LoginStatus status) {
        this.status = status;
    }

    @Override
    public LoginStatus getResult() {
        return status;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '@' + status + '@' + Integer.toHexString(hashCode());
    }
}
