package com.triangleleft.flashcards.ui.login;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.service.IListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.ILoginRequest;
import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.IProvider;
import com.triangleleft.flashcards.service.SimpleLoginRequest;
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
    private final IProvider<ILoginRequest, ILoginResult> loginModule;
    private ILoginView view;
    private CommonError error;
    private ILoginRequest loginRequest;

    private IListener loginListener = new IListener() {
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
        loginRequest = new SimpleLoginRequest(login, password);
        loginModule.doRequest(loginRequest, loginListener);
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
        if (loginRequest != null) {
            loginModule.unregisterListener(loginRequest, loginListener);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        if (loginRequest != null) {
            loginModule.registerListener(loginRequest, loginListener);
        }
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
            restoreState(state);
        }
    }

    private void restoreState(ILoginView.LoginViewState state) {
        setCurrentState(state);
        switch (state) {
            case ENTER_CREDENTIAL:
                // Restore error messages, if any
                break;
            case PROGRESS:
                // We were waiting for request to complete
                // Do nothing, listener is guaranteed to be called
                break;
            default:
                AssertDialog.fail("Unknown state: " + state);
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
