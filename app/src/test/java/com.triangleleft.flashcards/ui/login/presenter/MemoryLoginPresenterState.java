package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.ui.login.view.LoginViewState;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MemoryLoginPresenterState implements ILoginPresenterState {
    private LoginViewState state;
    private Credentials credentials;
    private CommonError error;
    private ILoginRequest request;

    @Override
    public void setViewState(@NonNull LoginViewState state) {
        this.state = state;
    }

    @NonNull
    @Override
    public LoginViewState getViewState() {
        return state;
    }

    @Override
    public void setCredentials(@NonNull Credentials credentials) {
        this.credentials = credentials;
    }

    @NonNull
    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public void setError(@Nullable CommonError error) {
        this.error = error;
    }

    @Nullable
    @Override
    public CommonError getError() {
        return error;
    }

    @Override
    public void setRequest(@Nullable ILoginRequest loginRequest) {
        this.request = loginRequest;
    }

    @Nullable
    @Override
    public ILoginRequest getRequest() {
        return request;
    }
}
