package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.rest.LoginResponseModel;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestLoginModuleTest {

    private static final String LOGIN = "login";
    private static final String PASS = "pass";

    @Mock
    RestService service;
    @Mock
    SettingsModule settingsModule;
    @Mock
    AccountModule accountModule;
    private RestLoginModule module;

    @Before
    public void before() throws IOException {
        module = new RestLoginModule(service, settingsModule, accountModule);
    }

    @Test
    public void successfulLogin() {
        LoginResponseModel model = mock(LoginResponseModel.class);
        when(model.isSuccess()).thenReturn(true);
        when(model.getUserId()).thenReturn("id");
        when(service.login(anyString(), anyString())).thenReturn(Observable.just(model));
        when(settingsModule.loadUserData()).thenReturn(Observable.just(null));

        doLogin();

        verify(settingsModule).loadUserData();
        verify(accountModule).setUserId("id");
    }

    @Test
    public void loginErrorWouldBePassed() {
        Exception exception = new Exception();
        LoginResponseModel model = mock(LoginResponseModel.class);
        when(model.isSuccess()).thenReturn(false);
        when(model.getError()).thenReturn(exception);
        when(service.login(anyString(), anyString())).thenReturn(Observable.just(model));

        TestSubscriber<Void> subscriber = doLogin();

        subscriber.assertError(exception);
    }

    @Test
    public void loginHttpException() {
        HttpException exception = new HttpException(Response.success(null));
        when(service.login(anyString(), anyString())).thenReturn(Observable.error(exception));

        TestSubscriber<Void> subscriber = doLogin();

        subscriber.assertError(ServerException.class);
    }

    @Test
    public void loginIOException() {
        IOException exception = new IOException();
        when(service.login(anyString(), anyString())).thenReturn(Observable.error(exception));

        TestSubscriber<Void> subscriber = doLogin();

        subscriber.assertError(NetworkException.class);
    }

    @Test
    public void loginIsRemembered() {
        when(service.login(anyString(), anyString())).thenReturn(Observable.empty());

        doLogin();

        verify(accountModule).setLogin(LOGIN);
    }

    private TestSubscriber<Void> doLogin() {
        TestSubscriber<Void> subscriber = TestSubscriber.create();
        module.login(LOGIN, PASS).subscribe(subscriber);
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);
        return subscriber;
    }

}
