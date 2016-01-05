package com.triangleleft.flashcards.login.presenter;

import com.triangleleft.flashcards.login.view.ILoginView;
import com.triangleleft.flashcards.presenter.IPresenter;

public interface ILoginPresenter extends IPresenter<ILoginView> {
    void onLoginClick(String login, String email);
}
