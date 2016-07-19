package com.triangleleft.flashcards.login;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.service.settings.UserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    AccountModule accountModule;
    @Mock
    LoginModule loginModule;
    @Mock
    ILoginView view;
    @Mock
    ILoginResult loginResult;
    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    private LoginPresenter presenter;

    @Before
    public void before() {
        presenter = new LoginPresenter(accountModule, loginModule, Schedulers.immediate());
        // By default, we need to login
        when(accountModule.shouldRememberUser()).thenReturn(false);
        when(accountModule.getLogin()).thenReturn(Optional.empty());
        when(accountModule.getUserData()).thenReturn(Optional.empty());
        when(accountModule.getUserId()).thenReturn(Optional.empty());
    }

    @Test
    public void advanceImmediatelyIfAlreadyLogged() {
        when(accountModule.shouldRememberUser()).thenReturn(true);
        when(accountModule.getUserData()).thenReturn(Optional.of(mock(UserData.class)));
        when(accountModule.getUserId()).thenReturn(Optional.of("userId"));

        initPresenter();

        verify(view).advance();
    }

    @Test
    public void loginErrorClearedOnNewLogin() {
        initPresenter();
        reset(view);

        presenter.onLoginChanged("value");
        verify(view).setLoginErrorVisible(false);
    }

    @Test
    public void passwordErrorClearedOnNewPassword() {
        initPresenter();
        reset(view);

        presenter.onPasswordChanged("value");
        verify(view).setPasswordErrorVisible(false);
    }

    @Test
    public void loginClick() {
        initPresenter();
        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.empty());

        presenter.onLoginClick();

        verify(view).showProgress();
    }

    @Test
    public void onRememberCheckWouldSaveItToAccountModule() {
        initPresenter();

        presenter.onRememberCheck(true);
        verify(accountModule).setRememberUser(true);
        presenter.onRememberCheck(false);
        verify(accountModule).setRememberUser(false);
    }

    @Test
    public void loginButtonEnabledOnlyForLoginAndPassword() {
        initPresenter();
        // Start disabled
        verify(view).setLoginButtonEnabled(false);
        presenter.onLoginChanged("login");

        verify(view, never()).setLoginButtonEnabled(true);

        presenter.onLoginChanged("");
        presenter.onPasswordChanged("password");
        verify(view, never()).setLoginButtonEnabled(true);

        presenter.onLoginChanged("login");
        presenter.onPasswordChanged("password");
        verify(view).setLoginButtonEnabled(true);
    }

    @Test
    public void handleLoginError() {
        initPresenter();
        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new LoginException()));

        presenter.onLoginClick();
        verify(view).setLoginErrorVisible(true);
    }

    @Test
    public void handlePasswordError() {
        initPresenter();
        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new PasswordException()));

        presenter.onLoginClick();
        verify(view).setPasswordErrorVisible(true);
    }

    @Test
    public void handleNetworkError() {
        initPresenter();
        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new NetworkException()));

        presenter.onLoginClick();
        verify(view).showNetworkError();
    }

    @Test
    public void handleGenericError() {
        initPresenter();
        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new ServerException()));

        presenter.onLoginClick();
        verify(view).showGenericError();
    }

    @Test
    public void onDestroyWouldUnsubscribe() {
        initPresenter();
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        Observable<Void> observable = Observable.empty();
        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
        when(loginModule.login(anyString(), anyString())).thenReturn(observable);

        presenter.onLoginClick();
        presenter.onDestroy();

        assertTrue(unsubscribed.get());
    }

    private void initPresenter() {
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onRebind(view);
    }
}
