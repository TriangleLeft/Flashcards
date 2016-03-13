package com.triangleleft.flashcards.mvp.login.presenter;

import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.mvp.common.presenter.IPresenterState;
import com.triangleleft.flashcards.mvp.login.view.LoginViewState;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ILoginPresenterState extends IPresenterState {

    void setViewState(@NonNull LoginViewState state);

    @NonNull
    LoginViewState getViewState();

    void setCredentials(@NonNull Credentials credentials);

    @NonNull
    Credentials getCredentials();

    void setError(@Nullable CommonError error);

    @Nullable
    CommonError getError();

    void setRequest(@Nullable ILoginRequest loginRequest);

    @Nullable
    ILoginRequest getRequest();
}
