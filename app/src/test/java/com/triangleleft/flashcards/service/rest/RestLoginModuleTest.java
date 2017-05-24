package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

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

    private RestLoginModule module;

    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);
        module = new RestLoginModule(service, settingsModule, accountModule);
    }

//    @Test
//    public void successfulLogin() {
//        LoginResponseModel model = mock(LoginResponseModel.class);
//        UserData mockUserData = mock(UserData.class);
//        when(model.isSuccess()).thenReturn(true);
//        when(model.getUserId()).thenReturn("id");
//        when(service.login(any(LoginRequestController.class))).thenReturn(Call.just(model));
//        when(settingsModule.userData()).thenReturn(Call.just(mockUserData));
//
//        TestObserver<Object> observer = new TestObserver<>();
//        module.login(LOGIN, PASS).enqueue(observer);
//
//        verify(accountModule).setUserData(mockUserData);
//        verify(accountModule).setUserId("id");
//        observer.assertOnNextCalled();
//    }
//
//    @Test
//    public void loginErrorWouldBePassed() {
//        Exception exception = new Exception();
//        LoginResponseModel model = mock(LoginResponseModel.class);
//        when(model.isSuccess()).thenReturn(false);
//        when(model.getError()).thenReturn(exception);
//        when(service.login(any(LoginRequestController.class))).thenReturn(Call.just(model));
//
//        TestObserver<Object> observer = new TestObserver<>();
//        module.login(LOGIN, PASS).enqueue(observer);
//
//        observer.assertError(exception);
//    }
//
//    @Test
//    public void loginHttpException() {
//        ServerException exception = new ServerException();
//        when(service.login(any(LoginRequestController.class))).thenReturn(Call.error(exception));
//
//        TestObserver<Object> observer = new TestObserver<>();
//        module.login(LOGIN, PASS).enqueue(observer);
//
//        observer.assertError(exception);
//    }
//
//    @Test
//    public void loginIOException() {
//        NetworkException exception = new NetworkException(new IOException());
//        when(service.login(any(LoginRequestController.class))).thenReturn(Call.error(exception));
//
//        TestObserver<Object> observer = new TestObserver<>();
//        module.login(LOGIN, PASS).enqueue(observer);
//
//        observer.assertError(exception);
//    }
//
//    @Test
//    public void loginIsRemembered() {
//        when(service.login(any(LoginRequestController.class))).thenReturn(Call.error(new Throwable()));
//
//        TestObserver<Object> observer = new TestObserver<>();
//        module.login(LOGIN, PASS).enqueue(observer);
//
//        verify(accountModule).setLogin(LOGIN);
//    }


}
