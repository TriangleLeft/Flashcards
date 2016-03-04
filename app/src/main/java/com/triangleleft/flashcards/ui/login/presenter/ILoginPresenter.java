package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.ui.common.presenter.IPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;

import android.support.annotation.NonNull;

public interface ILoginPresenter extends IPresenter<ILoginView, ILoginPresenterState> {

    void onLoginChanged(@NonNull String login);

    void onPasswordChanged(@NonNull String password);

    void onLoginClick();
}
