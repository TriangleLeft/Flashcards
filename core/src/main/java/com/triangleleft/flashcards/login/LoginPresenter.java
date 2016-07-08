package com.triangleleft.flashcards.login;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

@FunctionsAreNonnullByDefault
@ActivityScope
public class LoginPresenter extends AbstractPresenter<ILoginView> {

    private static final Logger logger = LoggerFactory.getLogger(LoginPresenter.class);

    private final LoginModule loginModule;
    private final Scheduler mainThreadScheduler;
    private final AccountModule accountModule;
    private Subscription subscription = Subscriptions.empty();
    private String login = "";
    private String password = "";
    private boolean rememberUser = false;

    @Inject
    public LoginPresenter(AccountModule accountModule, LoginModule loginModule,
                          Scheduler mainThreadScheduler) {
        super(ILoginView.class);
        this.accountModule = accountModule;
        this.loginModule = loginModule;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate()");
        rememberUser = accountModule.shouldRememberUser();
        // If we are already logged, and we have saved user data, advance immediately
        if (rememberUser && accountModule.getUserData().isPresent()) {
            getView().advance();
        }
    }

    @Override
    public void onBind(ILoginView view) {
        super.onBind(view);
        view.setLogin(login);
        view.setPassword(password);
        view.setRememberUser(rememberUser);
        // TODO: Set error
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        subscription.unsubscribe();
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
        getView().showProgress();
        subscription = loginModule.login(login, password)
                .observeOn(mainThreadScheduler)
                .subscribe(nothing -> getView().advance(), this::handleError);
    }

    public void onRememberCheck(boolean checked) {
        rememberUser = checked;
        accountModule.setRememberUser(checked);
    }

    private void handleError(Throwable error) {
        getView().showContent();
        if (error instanceof LoginException) {
            getView().setLoginError(error.getMessage());
        } else if (error instanceof PasswordException) {
            getView().setPasswordError(error.getMessage());
        } else if (error instanceof NetworkException) {
            getView().showNetworkError();
        } else {
            getView().showGenericError();
        }
    }

}
