package com.triangleleft.flashcards.login;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.UserData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ILoginPresenterImplTest {

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
        presenter = new LoginPresenter(accountModule, loginModule);
        // By default, we need to login
        when(accountModule.shouldRememberUser()).thenReturn(false);
    }

    @Test
    public void advanceImmediatelyIfAlreadyLogged() {
        when(accountModule.shouldRememberUser()).thenReturn(true);
        UserData data = mock(UserData.class);
        when(accountModule.getUserData()).thenReturn(Optional.of(data));

        initPresenter();

        verify(view).advance();
    }

    @Test
    public void loginErrorClearedOnNewLogin() {
        initPresenter();

        presenter.onLoginChanged("value");
        verify(view).setLoginError(isNull(String.class));
    }

    @Test
    public void passwordErrorClearedOnNewPassword() {
        initPresenter();

        presenter.onPasswordChanged("value");
        verify(view).setPasswordError(isNull(String.class));
    }

    @Test
    public void loginClick() {
        initPresenter();

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

    private void initPresenter() {
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onRebind(view);
    }
}
