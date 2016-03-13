package com.triangleleft.flashcards.mvp.login.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.IPresenter;
import com.triangleleft.flashcards.mvp.login.view.ILoginView;

import android.support.annotation.NonNull;

public interface ILoginPresenter extends IPresenter<ILoginView> {

    void onLoginChanged(@NonNull String login);

    void onPasswordChanged(@NonNull String password);

    void onLoginClick();
}
