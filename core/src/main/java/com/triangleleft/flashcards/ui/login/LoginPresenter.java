package com.triangleleft.flashcards.ui.login;

import com.annimon.stream.Optional;
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
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;


@FunctionsAreNonnullByDefault
@ActivityScope
public class LoginPresenter extends AbstractPresenter<ILoginView, LoginViewState> {

    private static final Logger logger = LoggerFactory.getLogger(LoginPresenter.class);

    private final LoginModule loginModule;
    private final AccountModule accountModule;

    private final PublishSubject<String> logins = PublishSubject.create();
    private final PublishSubject<String> passwords = PublishSubject.create();
    private final PublishSubject<Boolean> rememberUserChecks = PublishSubject.create();
    private final PublishSubject<LoginEvent> loginEvents = PublishSubject.create();
    private final BehaviorSubject<LoginViewState> viewStates = BehaviorSubject.create();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public LoginPresenter(AccountModule accountModule, LoginModule loginModule,
                          @Named(VIEW_EXECUTOR) Executor executor) {
        super(ILoginView.class, executor);
        this.accountModule = accountModule;
        this.loginModule = loginModule;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public LoginViewState getViewState() {
        return viewStates.getValue();
    }

    @Override
    public void onCreate(Optional<LoginViewState> savedViewState) {
        logger.debug("onCreate() called with savedViewState = [{}]", savedViewState);

        LoginViewState initialState = savedViewState.orElseGet(this::getInitialState);

        Observable.merge(
                logins.map(LoginViewActions::setLogin),
                passwords.map(LoginViewActions::setPassword),
                rememberUserChecks.map(LoginViewActions::setRememberUser),
                loginEvents.compose(getLoginEventTransformer()))
                .scan(initialState, (viewState, viewAction) -> viewAction.reduce(viewState))
                .distinctUntilChanged()
                .subscribe(state -> {
                    logger.debug("onNext() {}", state);
                    viewStates.onNext(state);
                });

        viewStates.onNext(initialState);
    }

    @Override
    public void onBind(ILoginView view) {
        super.onBind(view);
    }

    @Override
    public synchronized void onRebind(ILoginView view) {
        super.onRebind(view);

        disposable = new CompositeDisposable();
        // Connect view observables to our publish subjects
        // Connect view to our behavior subject
        disposable.addAll(
                viewStates.subscribe(view::render),
                view.logins().subscribe(logins::onNext),
                view.passwords().subscribe(passwords::onNext),
                view.rememberUserChecks().subscribe(rememberUserChecks::onNext),
                view.loginEvents().subscribe(loginEvents::onNext)
        );
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

    private ObservableTransformer<LoginEvent, ViewAction<LoginViewState>> getLoginEventTransformer() {
        return events -> events
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
    }

    private LoginViewState getInitialState() {
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

        return builder.build();
    }
}
