package com.triangleleft.flashcards.ui.login;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class LoginPresenterTest {

    @Mock
    AccountModule accountModule;
    @Mock
    LoginModule loginModule;
    @Mock
    Call<Object> mockCall;
    @Mock
    LoginView view;
    private LoginPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(accountModule, loginModule, Runnable::run);
        // By default, we need to login
        when(accountModule.shouldRememberUser()).thenReturn(false);
        when(accountModule.getLogin()).thenReturn(Optional.empty());
        when(accountModule.getUserData()).thenReturn(Optional.empty());
        when(accountModule.getUserId()).thenReturn(Optional.empty());
    }

//    @Test
//    public void advanceImmediatelyIfAlreadyLogged() {
//        when(accountModule.shouldRememberUser()).thenReturn(true);
//        when(accountModule.getUserData()).thenReturn(Optional.of(mock(UserData.class)));
//        when(accountModule.getUserId()).thenReturn(Optional.of("userId"));
//
//        initPresenter();
//
//        verify(view).advance();
//    }
//
//    @Test
//    public void loginErrorClearedOnNewLogin() {
//        initPresenter();
//        reset(view);
//
//        presenter.onLoginChanged("value");
//        verify(view).setLoginErrorVisible(false);
//    }
//
//    @Test
//    public void passwordErrorClearedOnNewPassword() {
//        initPresenter();
//        reset(view);
//
//        presenter.onPasswordChanged("value");
//        verify(view).setPasswordErrorVisible(false);
//    }
//
//    @Test
//    public void loginClickWouldShowProgress() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Call.empty());
//
//        presenter.onLoginClick();
//
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void loginClickWouldAdvanceOnSuccess() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Call.just(new Object()));
//
//        presenter.onLoginClick();
//
//        verify(view).advance();
//    }
//
//    @Test
//    public void onRememberCheckWouldSaveItToAccountModule() {
//        initPresenter();
//
//        presenter.onRememberCheck(true);
//        verify(accountModule).setRememberUser(true);
//        presenter.onRememberCheck(false);
//        verify(accountModule).setRememberUser(false);
//    }
//
//    @Test
//    public void loginButtonEnabledOnlyForLoginAndPassword() {
//        initPresenter();
//        // Start disabled
//        verify(view).setLoginButtonEnabled(false);
//
//        presenter.onLoginChanged("login");
//        verify(view, never()).setLoginButtonEnabled(true);
//
//        presenter.onLoginChanged("");
//        presenter.onPasswordChanged("password");
//        verify(view, never()).setLoginButtonEnabled(true);
//
//        presenter.onLoginChanged("login");
//        presenter.onPasswordChanged("password");
//        verify(view).setLoginButtonEnabled(true);
//    }
//
//    @Test
//    public void handleLoginError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Call.error(new LoginException()));
//
//        presenter.onLoginClick();
//        verify(view).setLoginErrorVisible(true);
//    }
//
//    @Test
//    public void handlePasswordError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Call.error(new PasswordException()));
//
//        presenter.onLoginClick();
//        verify(view).setPasswordErrorVisible(true);
//    }
//
//    @Test
//    public void handleNetworkError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString()))
//                .thenReturn(Call.error(new NetworkException()));
//
//        presenter.onLoginClick();
//        verify(view).notifyNetworkError();
//    }
//
//    @Test
//    public void handleGenericError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString()))
//                .thenReturn(Call.error(new ServerException()));
//
//        presenter.onLoginClick();
//        verify(view).notifyGenericError();
//    }
//
//    @Ignore
//    @Test
//    public void onDestroyWouldCancel() {
//        initPresenter();
//        AtomicBoolean canceled = new AtomicBoolean(false);
//        doAnswer(invocation -> {
//            canceled.set(true);
//            return null;
//        }).when(mockCall).cancel();
//        when(loginModule.login(anyString(), anyString())).thenReturn(mockCall);
//
//        presenter.onLoginClick();
//        presenter.onDestroy();
//
//        assertThat(canceled.get(), is(true));
//    }
//
//    private void initPresenter() {
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onRebind(view);
//    }
}
