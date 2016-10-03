package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

@Ignore
@RunWith(JUnit4.class)
public class LoginPresenterTest {

    @Mock
    AccountModule accountModule;
    @Mock
    LoginModule loginModule;
    @Mock
    ILoginView view;

    private LoginPresenter presenter;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//        presenter = new LoginPresenter(accountModule, loginModule, Schedulers.immediate());
//        // By default, we need to login
//        when(accountModule.shouldRememberUser()).thenReturn(false);
//        when(accountModule.getLogin()).thenReturn(Optional.empty());
//        when(accountModule.getUserData()).thenReturn(Optional.empty());
//        when(accountModule.getUserId()).thenReturn(Optional.empty());
//    }
//
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
//        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.empty());
//
//        presenter.onLoginClick();
//
//        verify(view).showProgress();
//    }
//
//    @Test
//    public void loginClickWouldAdvanceOnSuccess() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.just(null));
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
//        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new LoginException()));
//
//        presenter.onLoginClick();
//        verify(view).setLoginErrorVisible(true);
//    }
//
//    @Test
//    public void handlePasswordError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString())).thenReturn(Observable.error(new PasswordException()));
//
//        presenter.onLoginClick();
//        verify(view).setPasswordErrorVisible(true);
//    }
//
//    @Test
//    public void handleNetworkError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString()))
//                .thenReturn(Observable.error(new NetworkException(new Throwable())));
//
//        presenter.onLoginClick();
//        verify(view).notifyNetworkError();
//    }
//
//    @Test
//    public void handleGenericError() {
//        initPresenter();
//        when(loginModule.login(anyString(), anyString()))
//                .thenReturn(Observable.error(new ServerException(new Throwable())));
//
//        presenter.onLoginClick();
//        verify(view).notifyGenericError();
//    }
//
//    @Test
//    public void onDestroyWouldUnsubscribe() {
//        initPresenter();
//        AtomicBoolean unsubscribed = new AtomicBoolean(false);
//        Observable<Void> observable = Observable.empty();
//        observable = observable.doOnUnsubscribe(() -> unsubscribed.set(true));
//        when(loginModule.login(anyString(), anyString())).thenReturn(observable);
//
//        presenter.onLoginClick();
//        presenter.onDestroy();
//
//        assertTrue(unsubscribed.get());
//    }
//
//    private void initPresenter() {
//        presenter.onCreate();
//        presenter.onBind(view);
//        presenter.onRebind(view);
//    }
}
