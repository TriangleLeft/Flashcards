package com.triangleleft.flashcards.service.stub;

import com.triangleleft.flashcards.Injector;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.ICommonListener;
import com.triangleleft.flashcards.service.error.LoginFieldError;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import android.content.SharedPreferences;
import android.os.Handler;

import javax.inject.Inject;

public class StubLoginModule implements ILoginModule {

    private static final String KEY_LOGGED = "key_logged";
    private static final String TAG = StubLoginModule.class.getSimpleName();
    @Inject
    SharedPreferences preferences;

    public StubLoginModule() {
        Injector.INSTANCE.getComponent().inject(this);
        isLogged = preferences.getBoolean(KEY_LOGGED, false);
    }

    private ICommonListener listener;
    private boolean isLogged;

    @Override
    public boolean isLogged() {
        return isLogged;
    }

    @Override
    public void login(final String login, final String password) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLoginValid = "admin".equals(login);
                boolean isPasswordValid = "admin".equals(password);
                if (isLoginValid && isPasswordValid) {
                    setLogged(true);
                    listener.onSuccess();
                } else {
                    setLogged(false);
                    LoginResponseModel.ErrorField field = isLoginValid ?
                            LoginResponseModel.ErrorField.PASSWORD :
                            LoginResponseModel.ErrorField.LOGIN;
                    listener.onError(new LoginFieldError("Failed", field));
                }
            }
        }, 2000);
    }

    @Override
    public void registerListener(ICommonListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener(ICommonListener listener) {
        if (this.listener == listener) {
            this.listener = null;
        }
    }

    private void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
        preferences.edit().putBoolean(KEY_LOGGED, isLogged).apply();
    }


}
