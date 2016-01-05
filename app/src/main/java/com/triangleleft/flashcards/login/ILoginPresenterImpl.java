package com.triangleleft.flashcards.login;

import com.triangleleft.flashcards.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.login.view.ILoginView;
import com.triangleleft.flashcards.service.ILoginModule;

import android.os.Bundle;

import javax.inject.Inject;

public class ILoginPresenterImpl implements ILoginPresenter {

    @Inject
    ILoginModule loginModule;

    @Override
    public void login(String login, String email) {

    }

    @Override
    public void onBind(ILoginView view) {
        if (loginModule.isLogged()) {
            // If we are logged, open main screen
            advanceToMainScreen();
        } else {
            // Check saved state
            if (savedInstanceState != null && savedInstanceState.getString(KEY_STATE) != null) {
                setState(LoginState.valueOf(savedInstanceState.getString(KEY_STATE)));
            } else {
                setState(LoginState.INPUT);
            }
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_STATE, state.name());
    }
}
