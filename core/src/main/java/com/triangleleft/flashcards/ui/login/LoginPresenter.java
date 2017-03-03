package com.triangleleft.flashcards.ui.login;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.Call;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.Disposable;


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
    private Call<Object> call = Call.empty();
    private List<Disposable> disposables = new ArrayList<>();

    @Inject
    public LoginPresenter(AccountModule accountModule, LoginModule loginModule,
                          @Named(VIEW_EXECUTOR) Executor executor) {
        super(ILoginView.class, executor);
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
    public synchronized void onRebind(ILoginView view) {
        super.onRebind(view);
        withDisposables(
                view.logins().subscribe(this::onLoginChanged),
                view.passwords().subscribe(this::onPasswordChanged),
                view.rememberUsers().subscribe(this::onRememberCheck),
                view.loginClicks().subscribe(click -> onLoginClick())
        );
    }

    @Override
    public void onUnbind() {
        super.onUnbind();
        Stream.of(disposables).forEach(Disposable::dispose);
    }

    private void withDisposables(Disposable... disposables) {
        this.disposables.addAll(Arrays.asList(disposables));
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        call.cancel();
    }

    private void updateLoginButton() {
        if (TextUtils.hasText(login) && TextUtils.hasText(password)) {
            getView().setLoginButtonEnabled(true);
        } else {
            getView().setLoginButtonEnabled(false);
        }
    }

    private void onLoginClick() {
        logger.debug("onLoginClick() called");
        getView().showProgress();
        call = loginModule.login(login, password);
        call.enqueue(
                data -> getView().advance(),
                error -> {
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
                });
    }


    private void onLoginChanged(@NonNull String newLogin) {
        logger.debug("onLoginChanged() called with: newLogin = [{}]", newLogin);
        login = newLogin;
        hasLoginError = false;
        getView().setLoginErrorVisible(hasLoginError);
        updateLoginButton();
    }

    private void onPasswordChanged(@NonNull String newPassword) {
        logger.debug("onPasswordChanged() called with: newPassword = [{}]", newPassword);
        password = newPassword;
        hasPasswordError = false;
        getView().setPasswordErrorVisible(hasPasswordError);
        updateLoginButton();
    }

    private void onRememberCheck(boolean checked) {
        logger.debug("onRememberCheck() called with: checked = [{}]", checked);
        rememberUser = checked;
        accountModule.setRememberUser(checked);
    }

}
