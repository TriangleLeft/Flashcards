package com.triangleleft.flashcards.android.login;

import com.triangleleft.flashcards.android.AbstractViewDelegate;
import com.triangleleft.flashcards.mvp.login.view.ILoginView;
import com.triangleleft.flashcards.mvp.login.view.ILoginViewDelegate;
import com.triangleleft.flashcards.mvp.login.view.LoginViewState;
import com.triangleleft.flashcards.service.login.Credentials;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LoginViewDelegate extends AbstractViewDelegate<ILoginView> implements ILoginViewDelegate {
    @Override
    public void setLoginButtonEnabled(boolean enabled) {
        getHandler().post(() -> getView().setLoginButtonEnabled(enabled));
    }

    @Override
    public void setCredentials(@NonNull Credentials credentials) {
        getHandler().post(() -> getView().setCredentials(credentials));
    }

    @Override
    public void setLoginError(@Nullable String error) {
        getHandler().post(() -> getView().setLoginError(error));
    }

    @Override
    public void setPasswordError(@Nullable String error) {
        getHandler().post(() -> getView().setPasswordError(error));
    }

    @Override
    public void setGenericError(@Nullable String error) {
        getHandler().post(() -> getView().setGenericError(error));
    }

    @Override
    public void setState(@NonNull LoginViewState state) {
        getHandler().post(() -> getView().setState(state));
    }

    @Override
    public void advance() {
        getHandler().post(() -> getView().advance());
    }
}
