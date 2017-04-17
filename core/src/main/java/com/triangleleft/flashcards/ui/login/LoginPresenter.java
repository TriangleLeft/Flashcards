package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;


@FunctionsAreNonnullByDefault
@ActivityScope
public class LoginPresenter extends AbstractPresenter<ILoginView> {

    private static final Logger logger = LoggerFactory.getLogger(LoginPresenter.class);

    private final LoginModule loginModule;
    private final AccountModule accountModule;

    private CompositeDisposable disposable = new CompositeDisposable();

    private LoginViewState viewState;

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
        String login = accountModule.getLogin().orElse("");
        boolean rememberUser = accountModule.shouldRememberUser();

        LoginViewState.Builder builder = LoginViewState.builder()
                .login(login)
                .password("")
                .hasLoginError(false)
                .hasPasswordError(false)
                .loginButtonEnabled(false)
                .genericError("")
                .shouldRememberUser(rememberUser);

        // If we are already logged, and we have saved user data, advance immediately
        if (rememberUser && accountModule.getUserData().isPresent() && accountModule.getUserId().isPresent()) {
            builder.page(LoginViewState.Page.SUCCESS);
        } else {
            builder.page(LoginViewState.Page.CONTENT);
        }

        viewState = builder.build();
    }

    @Override
    public void onBind(ILoginView view) {
        super.onBind(view);
        // Get view bundle?

        view.render(viewState);
    }

    @Override
    public synchronized void onRebind(ILoginView view) {
        super.onRebind(view);

        ObservableTransformer<LoginEvent, ViewAction<LoginViewState>> loginEventTransformer = events -> events
                .flatMap(event -> loginModule.login(event.login(), event.password())
                        .map(ignored -> LoginViewActions.success())
                        .onErrorReturn(error -> {
                            if (error instanceof LoginException) {
                                return LoginViewActions.loginError();
                            } else if (error instanceof PasswordException) {
                                return LoginViewActions.passwordError();
                            } else {
                                logger.error("login error", error);
                                return LoginViewActions.genericError("error message");
                            }
                        })
                        .startWith(LoginViewActions.progress()));

        disposable.addAll(
                Observable.merge(
                        view.logins().map(LoginViewActions::setLogin),
                        view.passwords().map(LoginViewActions::setPassword),
                        view.rememberUserChecks().map(LoginViewActions::setRemememberUser),
                        view.loginEvents().compose(loginEventTransformer))
                        .scan(viewState, (viewState, viewAction) -> viewAction.reduce(viewState))
                        .distinctUntilChanged()
                        .subscribe(viewState -> {
                            // Update current view state
                            this.viewState = viewState;
                            getView().render(viewState);
                        }, error -> {
                            logger.error("log", error);
                        }));
    }

    @Override
    public void onUnbind() {
        super.onUnbind();
        disposable.dispose();
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
    }
}
