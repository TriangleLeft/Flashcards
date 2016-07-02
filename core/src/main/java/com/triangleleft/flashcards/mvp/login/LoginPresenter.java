package com.triangleleft.flashcards.mvp.login;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.login.SimpleLoginRequest;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
@ActivityScope
public class LoginPresenter extends AbstractPresenter<ILoginView> {

    private static final Logger logger = LoggerFactory.getLogger(LoginPresenter.class);

    private final LoginModule loginModule;
    private final SettingsModule settingsModule;
    private ILoginRequest loginRequest;

    private IListener<ILoginResult> loginListener = new LoginListener();
    private Credentials credentials = new Credentials();


    @Inject
    public LoginPresenter(LoginModule loginModule, SettingsModule settingsModule) {
        super(ILoginView.class);
        this.settingsModule = settingsModule;
        this.loginModule = loginModule;
        credentials.setLogin(loginModule.getLogin());
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() ");
        // If we are already logged, and we have saved user data, advance immediately
        if (loginModule.getLoginStatus() == LoginStatus.LOGGED && settingsModule.getCurrentUserData() != null) {
            getView().advance();
        }
    }

    public void onLoginChanged(@NonNull String login) {
        logger.debug("onLoginChanged() called with: login = [{}]", login);
        if (TextUtils.notEquals(credentials.getLogin(), login)) {
            credentials.setLogin(login);
            getView().setLogin(login);
            getView().setLoginError(null);
        }
    }

    public void onPasswordChanged(@NonNull String password) {
        logger.debug("onPasswordChanged() called with: password = [{}]", password);
        if (TextUtils.notEquals(credentials.getPassword(), password)) {
            credentials.setPassword(password);
            getView().setPassword(password);
            getView().setPasswordError(null);
        }
    }

    public void onLoginClick() {
        logger.debug("onLoginClick() called");
        getView().setState(LoginViewStatePage.PROGRESS);
        Preconditions.checkState(loginRequest == null, "Doing login request while another one is pending");
        loginRequest = new SimpleLoginRequest(credentials);
        loginModule.login(loginRequest, loginListener);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        final ILoginRequest request = loginRequest;
        if (request != null) {
            loginModule.cancelRequest(request);
            loginRequest = null;
        }
    }

    private void handleError(CommonError error) {
        getView().setState(LoginViewStatePage.ENTER_CREDENTIAL);
        String message = error.getMessage();
        // TODO: probably we don't want to rely on messages here and use predefined strings instead
        switch (error.getType()) {
            case LOGIN:
                getView().setLoginError(message);
                break;
            case PASSWORD:
                getView().setPasswordError(message);
                break;
            default:
                getView().setGenericError(message);
                break;
        }

    }

    private class LoginListener implements IListener<ILoginResult> {

        @Override
        public void onResult(@NonNull ILoginResult result) {
            logger.debug("onResult() called with: result = [{}]", result);
            loginRequest = null;

            // TODO: move getting initial settings here
            Preconditions
                    .checkState(result.getResult() == LoginStatus.LOGGED, "Got unknown status: " + result.getResult());
            // Advance to next screen
            getView().advance();
        }

        @Override
        public void onFailure(@NonNull CommonError error) {
            logger.debug("onFailure() called with: error = [{}]", error);
            loginRequest = null;
            handleError(error);
        }
    }

}
