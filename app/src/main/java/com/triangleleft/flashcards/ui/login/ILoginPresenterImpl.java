package com.triangleleft.flashcards.ui.login;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.service.ICommonListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

public class ILoginPresenterImpl implements ILoginPresenter {

    private static final String TAG = ILoginPresenterImpl.class.getSimpleName();
    private static final String KEY_STATE = TAG + "key_state";

    private ILoginView.LoginViewState currentState = ILoginView.LoginViewState.ENTER_CREDENTIAL;
    private final ILoginModule loginModule;
    private ILoginView view;
    private CommonError error;

    private ICommonListener loginListener = new ICommonListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess() called");
            advance();
        }

        @Override
        public void onError(@NonNull CommonError error) {
            Log.d(TAG, "onError() called with: " + "error = [" + error + "]");
            handleError(error);
        }
    };

    @Inject
    public ILoginPresenterImpl(ILoginModule loginModule) {
        Log.d(TAG, "ILoginPresenterImpl() called with: " + "loginModule = [" + loginModule + "]");
        this.loginModule = loginModule;
    }

    @Override
    public void onLoginClick(@NonNull String login, @NonNull String password) {
        Log.d(TAG, "onLoginClick() called with: " + "login = [" + login + "], password = [" + password + "]");
        setCurrentState(ILoginView.LoginViewState.PROGRESS);
        loginModule.login(login, password);
    }

    @Override
    public void onBind(@NonNull ILoginView view) {
        Log.d(TAG, "onBind() called with: " + "view = [" + view + "]");
        this.view = view;
        setCurrentState(currentState);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        loginModule.unregisterListener(loginListener);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        loginModule.registerListener(loginListener);
        if (loginModule.isLogged()) {
            advance();
        }
        // Else, we have to wait for listener
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = [" + outState + "]");
        outState.putString(KEY_STATE, currentState.name());
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle inState) {
        Log.d(TAG, "onRestoreInstanceState() called with: " + "inState = [" + inState + "]");
        if (inState != null) {
            AssertDialog.assertNotNull(inState.getString(KEY_STATE), "Got null value for state");
            ILoginView.LoginViewState state = ILoginView.LoginViewState.valueOf(inState.getString(KEY_STATE));
            switch (state) {
                case ENTER_CREDENTIAL:
                    // Restore error messages, if any
                    break;
                case PROGRESS:
                    // No need to change view here
                    break;
                default:
                    AssertDialog.fail("Unknown state: " + state);
            }
            setCurrentState(state);
        }
    }


    private void advance() {
        Log.d(TAG, "advance()");
        AssertDialog.assertNotNull(view, "calling advance() while not binded to view");
        view.advance();
    }

    private void handleError(CommonError error) {
        this.error = error;
        FlashcardsApplication.showDebugToast(error.getMessage());
        setCurrentState(ILoginView.LoginViewState.ENTER_CREDENTIAL);
    }

    private void setCurrentState(@NonNull ILoginView.LoginViewState state) {
        Log.d(TAG, "setCurrentState() called with: " + "state = [" + state + "]");
        currentState = state;
        AssertDialog.assertNotNull(view, "View was null");
        view.setState(state);
    }

}
