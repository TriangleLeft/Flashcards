package com.triangleleft.flashcards.mvp.login;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginModule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ILoginPresenterImplLoginTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Mock
    LoginModule module;

    @Mock
    ILoginView view;

    @Mock
    ILoginResult loginResult;

    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    @Captor
    ArgumentCaptor<ILoginRequest> requestCaptor;

    LoginPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(module);
        presenter.onRebind(view);
        // Set login and password
        presenter.onLoginChanged(LOGIN);
        presenter.onPasswordChanged(PASSWORD);
        // Click login
        presenter.onLoginClick();
        // Verify that do request is called, and capture listener
        verify(module).login(requestCaptor.capture(), listenerCaptor.capture());
        // Verify progress is shown
        verify(view).setState(LoginViewStatePage.PROGRESS);
    }


//    @Test
//    public void successfulLogin() {
//        // Call listener
//        listenerCaptor.getValue().onResult(new SimpleLoginResult(LoginStatus.LOGGED));
//        // Verify that we would advance to next screen
//        verify(view).advance();
//    }
//
//    @Test
//    public void wrongLogin() {
//        // Call listener
//        String errorMessage = "error";
//        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.LOGIN, errorMessage));
//        // Verify that we show enter creds form
//        verify(view).setState(LoginViewStatePage.ENTER_CREDENTIAL);
//        // Verify that error message is propagated
//        verify(view).setLoginError(errorMessage);
//    }
//
//    @Test
//    public void wrongPassword() {
//        // Call listener
//        String errorMessage = "error";
//        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.PASSWORD, errorMessage));
//        // Verify that we show enter creds form
//        verify(view).setState(LoginViewStatePage.ENTER_CREDENTIAL);
//        // Verify that error message is propagated
//        verify(view).setPasswordError(errorMessage);
//    }
//
//    @Test
//    public void networkError() {
//        // Call listener
//        String errorMessage = "error";
//        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.NETWORK, errorMessage));
//        // Verify that we show enter creds form
//        verify(view).setState(LoginViewStatePage.ENTER_CREDENTIAL);
//        // Verify that error message is propagated
//        verify(view).setGenericError(errorMessage);
//    }
//
//    @Test
//    public void reattachListener() {
//        presenter.onPause();
//        // Verify listener detached with current request and listener
//        verify(module).unregisterListener(requestCaptor.getValue(), listenerCaptor.getValue());
//        presenter.onResume();
//        // Verify listener attached
//        verify(module).registerListener(requestCaptor.getValue(), listenerCaptor.getValue());
//    }
//
//    @Test
//    public void saveRequestState() {
//        ILoginPresenterState state = new com.triangleleft.flashcards.mvp.login.presenter.MemoryLoginPresenterState();
//        presenter.onSaveInstanceState(state);
//
//        assertEquals(LoginViewStatePage.PROGRESS, state.getViewState());
//        assertEquals(LOGIN, state.getCredentials().getLogin());
//        assertEquals(PASSWORD, state.getCredentials().getPassword());
//        assertEquals(requestCaptor.getValue(), state.getRequest());
//        assertNull(state.getError());
//    }
//
//    @Test
//    public void saveLoginState() {
//        String errorMessage = "error";
//        CommonError error = new CommonError(ErrorType.LOGIN, errorMessage);
//        listenerCaptor.getValue().onFailure(error);
//
//        ILoginPresenterState state = new com.triangleleft.flashcards.mvp.login.presenter.MemoryLoginPresenterState();
//        presenter.onSaveInstanceState(state);
//
//        assertEquals(LoginViewStatePage.ENTER_CREDENTIAL, state.getViewState());
//        assertEquals(LOGIN, state.getCredentials().getLogin());
//        assertEquals(PASSWORD, state.getCredentials().getPassword());
//        assertNull(state.getRequest());
//        assertEquals(error, state.getError());
//    }
//
//    @Test
//    public void doNotSaveGenericError() {
//        String errorMessage = "error";
//        CommonError error = new CommonError(ErrorType.NETWORK, errorMessage);
//        listenerCaptor.getValue().onFailure(error);
//
//        ILoginPresenterState state = new com.triangleleft.flashcards.mvp.login.presenter.MemoryLoginPresenterState();
//        presenter.onSaveInstanceState(state);
//
//        assertEquals(LoginViewStatePage.ENTER_CREDENTIAL, state.getViewState());
//        assertEquals(LOGIN, state.getCredentials().getLogin());
//        assertEquals(PASSWORD, state.getCredentials().getPassword());
//        assertNull(state.getRequest());
//        assertNull(state.getError());
//    }

}
