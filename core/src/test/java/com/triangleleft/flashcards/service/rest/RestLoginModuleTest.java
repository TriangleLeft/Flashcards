package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.Calls;
import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.rest.LoginRequestController;
import com.triangleleft.flashcards.service.login.rest.LoginResponseModel;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.TestObserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class RestLoginModuleTest {

    private static final String LOGIN = "login";
    private static final String PASS = "pass";

    @Mock
    RestService service;
    @Mock
    SettingsModule settingsModule;
    @Mock
    AccountModule accountModule;
    @Captor
    ArgumentCaptor<Observer<UserData>> userDataCaptor;
    private RestLoginModule module;

    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);
        module = new RestLoginModule(service, settingsModule, accountModule);
    }

    @Test
    public void successfulLogin() {
        LoginResponseModel model = mock(LoginResponseModel.class);
        UserData mockUserData = mock(UserData.class);
        when(model.isSuccess()).thenReturn(true);
        when(model.getUserId()).thenReturn("id");
        when(service.login(any(LoginRequestController.class))).thenReturn(Calls.just(model));
        doAnswer(mock -> {
            userDataCaptor.getValue().onNext(mockUserData);
            return null;
        }).when(settingsModule).loadUserData(userDataCaptor.capture());

        TestObserver<Void> observer = new TestObserver<>();
        module.login(LOGIN, PASS, observer);

        verify(accountModule).setUserData(mockUserData);
        verify(accountModule).setUserId("id");
        observer.assertOnNextCalled();
    }

    @Test
    public void loginErrorWouldBePassed() {
        Exception exception = new Exception();
        LoginResponseModel model = mock(LoginResponseModel.class);
        when(model.isSuccess()).thenReturn(false);
        when(model.getError()).thenReturn(exception);
        when(service.login(any(LoginRequestController.class))).thenReturn(Calls.just(model));

        TestObserver<Void> observer = new TestObserver<>();
        module.login(LOGIN, PASS, observer);

        observer.assertError(exception);
    }

    @Test
    public void loginHttpException() {
        ServerException exception = new ServerException();
        when(service.login(any(LoginRequestController.class))).thenReturn(Calls.error(exception));

        TestObserver<Void> observer = new TestObserver<>();
        module.login(LOGIN, PASS, observer);

        observer.assertError(exception);
    }

    @Test
    public void loginIOException() {
        NetworkException exception = new NetworkException(new IOException());
        when(service.login(any(LoginRequestController.class))).thenReturn(Calls.error(exception));

        TestObserver<Void> observer = new TestObserver<>();
        module.login(LOGIN, PASS, observer);

        observer.assertError(exception);
    }

    @Test
    public void loginIsRemembered() {
        when(service.login(any(LoginRequestController.class))).thenReturn(Calls.error(new Throwable()));

        TestObserver<Void> observer = new TestObserver<>();
        module.login(LOGIN, PASS, observer);

        verify(accountModule).setLogin(LOGIN);
    }



}
