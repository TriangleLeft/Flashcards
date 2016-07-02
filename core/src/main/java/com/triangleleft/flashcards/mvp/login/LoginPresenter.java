package com.triangleleft.flashcards.mvp.login;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.error.CommonError;
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
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;
    private ILoginRequest loginRequest;

    private IListener<ILoginResult> loginListener = new LoginListener();
    private String login = "";
    private String password = "";
    private boolean rememberUser = false;


    @Inject
    public LoginPresenter(AccountModule accountModule, LoginModule loginModule, SettingsModule settingsModule) {
        super(ILoginView.class);
        this.accountModule = accountModule;
        this.settingsModule = settingsModule;
        this.loginModule = loginModule;
        rememberUser = accountModule.shouldRememberUser();
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() ");
        // If we are already logged, and we have saved user data, advance immediately
        if (rememberUser && loginModule.getLoginStatus() == LoginStatus.LOGGED &&
                settingsModule.getCurrentUserData() != null) {
            getView().advance();
        }
    }

    @Override
    public void onBind(ILoginView view) {
        super.onBind(view);
        view.setLogin(login);
        view.setPassword(password);
        view.setRememberUser(rememberUser);
    }

    public void onLoginChanged(@NonNull String newLogin) {
        logger.debug("onLoginChanged() called with: newLogin = [{}]", newLogin);
        if (TextUtils.notEquals(login, newLogin)) {
            login = newLogin;
            getView().setLoginError(null);
        }
    }

    public void onPasswordChanged(@NonNull String newPassword) {
        logger.debug("onPasswordChanged() called with: newPassword = [{}]", newPassword);
        if (TextUtils.notEquals(password, newPassword)) {
            password = newPassword;
            getView().setPasswordError(null);
        }
    }

    public void onLoginClick() {
        logger.debug("onLoginClick() called");
        getView().setState(LoginViewStatePage.PROGRESS);
        Preconditions.checkState(loginRequest == null, "Doing login request while another one is pending");
        loginRequest = new SimpleLoginRequest(login, password);
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

    public void onRememberCheck(boolean checked) {
        rememberUser = checked;
        accountModule.setRememberUser(rememberUser);
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
