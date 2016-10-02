package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
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
    private String login = "";
    private String password = "";
    private boolean rememberUser = false;
    private boolean hasLoginError;
    private boolean hasPasswordError;

    @Inject
    public LoginPresenter(AccountModule accountModule, LoginModule loginModule) {
        super(ILoginView.class);
        this.accountModule = accountModule;
        this.loginModule = loginModule;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() called");
        login = accountModule.getLogin().orElse("");
        rememberUser = accountModule.shouldRememberUser();
        // If we are already logged, and we have saved user data, advance immediately
        if (rememberUser && accountModule.getUserData().isPresent() && accountModule.getUserId().isPresent()) {
            getView().advance();
        }
    }

    @Override
    public void onBind(ILoginView view) {
        super.onBind(view);
        // FIXME: What about showing progress or content?
        view.setLogin(login);
        view.setPassword(password);
        view.setRememberUser(rememberUser);
        view.setLoginErrorVisible(hasLoginError);
        view.setPasswordErrorVisible(hasPasswordError);
        updateLoginButton();
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");

    }

    public void onLoginChanged(@NonNull String newLogin) {
        logger.debug("onLoginChanged() called with: newLogin = [{}]", newLogin);
        if (TextUtils.notEquals(login, newLogin)) {
            login = newLogin;
            hasLoginError = false;
            getView().setLoginErrorVisible(hasLoginError);
            updateLoginButton();
        }
    }

    public void onPasswordChanged(@NonNull String newPassword) {
        logger.debug("onPasswordChanged() called with: newPassword = [{}]", newPassword);
        if (TextUtils.notEquals(password, newPassword)) {
            password = newPassword;
            hasPasswordError = false;
            getView().setPasswordErrorVisible(hasPasswordError);
            updateLoginButton();
        }
    }

    private void updateLoginButton() {
        if (TextUtils.hasText(login) && TextUtils.hasText(password)) {
            getView().setLoginButtonEnabled(true);
        } else {
            getView().setLoginButtonEnabled(false);
        }
    }

    public void onLoginClick() {
        logger.debug("onLoginClick() called");
        getView().showProgress();
        loginModule.login(login, password, new Observer<Void>() {
            @Override
            public void onError(Throwable error) {
                if (error instanceof LoginException) {
                    hasLoginError = true;
                    getView().setLoginErrorVisible(true);
                } else if (error instanceof PasswordException) {
                    hasPasswordError = true;
                    getView().setPasswordErrorVisible(true);
                } else if (error instanceof NetworkException) {
                    getView().notifyNetworkError();
                } else {
                    getView().notifyGenericError();
                }
                getView().showContent();
            }

            @Override
            public void onNext(Void aVoid) {
                getView().advance();
            }
        });
    }

    public void onRememberCheck(boolean checked) {
        logger.debug("onRememberCheck() called with: checked = [{}]", checked);
        rememberUser = checked;
        accountModule.setRememberUser(checked);
    }

}
