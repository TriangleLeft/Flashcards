package com.triangleleft.flashcards.service.stub;

import com.triangleleft.flashcards.service.ICommonListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.error.LoginError;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

public class StubLoginModule implements ILoginModule {

    private static final String KEY_LOGGED = "key_logged";
    private static final String TAG = StubLoginModule.class.getSimpleName();

    private final SharedPreferences preferences;
    private ICommonListener listener;
    private boolean isLogged;

    @Inject
    public StubLoginModule(SharedPreferences preferences) {
        this.preferences = preferences;
        isLogged = preferences.getBoolean(KEY_LOGGED, false);
    }

    @Override
    public boolean isLogged() {
        Log.d(TAG, "isLogged() called with: " + "");
        return isLogged;
    }

    @Override
    public void login(@NonNull final String login, @NonNull final String password) {
        Log.d(TAG, "login() called with: " + "login = [" + login + "], password = [" + password + "]");
        new Handler().postDelayed(() -> {
            boolean isLoginValid = "admin".equals(login);
            boolean isPasswordValid = "admin".equals(password);
            if (isLoginValid && isPasswordValid) {
                setLogged(true);
                listener.onSuccess();
            } else {
                setLogged(false);
                String loginError = isLoginValid ? null : "Wrong login";
                String passwordError = isPasswordValid ? null : "passwordError";
                listener.onError(new LoginError.Builder().setMessage("Failed").setLoginErrorMessage(loginError)
                        .setPasswordErrorMessage(passwordError).build());
            }
        }, 2000);
    }

    @Override
    public void registerListener(@NonNull ICommonListener listener) {
        Log.d(TAG, "registerListener() called with: " + "listener = [" + listener + "]");
        this.listener = listener;
    }

    @Override
    public void unregisterListener(@NonNull ICommonListener listener) {
        Log.d(TAG, "unregisterListener() called with: " + "listener = [" + listener + "]");
        if (this.listener == listener) {
            this.listener = null;
        }
    }

    private void setLogged(boolean isLogged) {
        Log.d(TAG, "setLogged() called with: " + "isLogged = [" + isLogged + "]");
        this.isLogged = isLogged;
        preferences.edit().putBoolean(KEY_LOGGED, isLogged).apply();
    }


}
