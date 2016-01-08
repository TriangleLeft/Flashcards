package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.presenter.IPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;

import android.support.annotation.NonNull;

public interface ILoginPresenter extends IPresenter<ILoginView> {
    void onLoginClick(@NonNull String login, @NonNull String password);
}
