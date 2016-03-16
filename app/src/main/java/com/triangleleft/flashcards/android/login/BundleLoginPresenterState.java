package com.triangleleft.flashcards.android.login;

import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.mvp.login.view.ILoginPresenterState;
import com.triangleleft.flashcards.mvp.login.view.LoginViewState;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;

public class BundleLoginPresenterState implements ILoginPresenterState {

    private static final String KEY_STATE = "keyState";
    private static final String KEY_LOGIN = "keyLogin";
    private static final String KEY_PASSWORD = "keyPassword";
    private static final String KEY_ERROR = "keyError";
    private static final String KEY_REQUEST = "keyRequest";
    private final Bundle bundle;

    public BundleLoginPresenterState(@NonNull Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void setViewState(@NonNull LoginViewState state) {
        bundle.putString(KEY_STATE, state.name());
    }

    @NonNull
    @Override
    public LoginViewState getViewState() {
        String string = bundle.getString(KEY_STATE);
        checkState(string != null, "Couldn't get view state from bundle");
        return LoginViewState.valueOf(string);
    }

    @Override
    public void setCredentials(@NonNull Credentials credentials) {
        bundle.putString(KEY_LOGIN, credentials.getLogin());
        bundle.putString(KEY_PASSWORD, credentials.getPassword());
    }

    @NonNull
    @Override
    public Credentials getCredentials() {
        String login = bundle.getString(KEY_LOGIN);
        String password = bundle.getString(KEY_PASSWORD);
        checkState(login != null, "Couldn't get login from bundle");
        checkState(password != null, "Couldn't get password from bundle");
        return new Credentials(login, password);
    }

    @Override
    public void setError(@Nullable CommonError error) {
        bundle.putSerializable(KEY_ERROR, error);
    }

    @Nullable
    @Override
    public CommonError getError() {
        return (CommonError) bundle.getSerializable(KEY_ERROR);
    }

    @Override
    public void setRequest(@Nullable ILoginRequest loginRequest) {
        bundle.putSerializable(KEY_REQUEST, loginRequest);
    }

    @Nullable
    @Override
    public ILoginRequest getRequest() {
        return (ILoginRequest) bundle.getSerializable(KEY_REQUEST);
    }
}
