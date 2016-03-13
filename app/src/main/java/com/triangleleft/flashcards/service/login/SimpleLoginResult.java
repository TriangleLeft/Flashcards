package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SimpleLoginResult implements ILoginResult {

    private final LoginStatus status;

    public SimpleLoginResult(@Nullable LoginStatus status) {
        this.status = status;
    }

    @NonNull
    @Override
    public LoginStatus getResult() {
        return status;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '@' + status + '@' + Integer.toHexString(hashCode());
    }
}
