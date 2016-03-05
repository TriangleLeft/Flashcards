package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.SystemOutTree;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.login.SimpleLoginResult;
import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.ui.login.view.ILoginView;
import com.triangleleft.flashcards.ui.login.view.LoginViewState;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import timber.log.Timber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class ILoginPresenterImplLoginTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Mock
    ILoginModule module;

    @Mock
    ILoginView view;

    @Mock
    ILoginResult loginResult;

    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    @Captor
    ArgumentCaptor<ILoginRequest> requestCaptor;

    ILoginPresenterImpl presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new ILoginPresenterImpl(module);
        presenter.onBind(view);
        // Set login and password
        presenter.onLoginChanged(LOGIN);
        presenter.onPasswordChanged(PASSWORD);
        // Click login
        presenter.onLoginClick();
        // Verify that do request is called, and capture listener
        verify(module).doRequest(requestCaptor.capture(), listenerCaptor.capture());
        // Verify progress is shown
        verify(view).setState(LoginViewState.PROGRESS);
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        Timber.plant(new SystemOutTree());
    }

    @Test
    public void successfulLogin() {
        // Call listener
        listenerCaptor.getValue().onResult(new SimpleLoginResult(LoginStatus.LOGGED));
        // Verify that we would advance to next screen
        verify(view).advance();
    }

    @Test
    public void wrongLogin() {
        // Call listener
        String errorMessage = "error";
        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.LOGIN, errorMessage));
        // Verify that we show enter creds form
        verify(view).setState(LoginViewState.ENTER_CREDENTIAL);
        // Verify that error message is propagated
        verify(view).setLoginError(errorMessage);
    }

    @Test
    public void wrongPassword() {
        // Call listener
        String errorMessage = "error";
        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.PASSWORD, errorMessage));
        // Verify that we show enter creds form
        verify(view).setState(LoginViewState.ENTER_CREDENTIAL);
        // Verify that error message is propagated
        verify(view).setPasswordError(errorMessage);
    }

    @Test
    public void networkError() {
        // Call listener
        String errorMessage = "error";
        listenerCaptor.getValue().onFailure(new CommonError(ErrorType.NETWORK, errorMessage));
        // Verify that we show enter creds form
        verify(view).setState(LoginViewState.ENTER_CREDENTIAL);
        // Verify that error message is propagated
        verify(view).setGenericError(errorMessage);
    }

    @Test
    public void reattachListener() {
        presenter.onPause();
        // Verify listener detached with current request and listener
        verify(module).unregisterListener(requestCaptor.getValue(), listenerCaptor.getValue());
        presenter.onResume();
        // Verify listener attached
        verify(module).registerListener(requestCaptor.getValue(), listenerCaptor.getValue());
    }

    @Test
    public void saveRequestState() {
        ILoginPresenterState state = new MemoryLoginPresenterState();
        presenter.onSaveInstanceState(state);

        assertEquals(LoginViewState.PROGRESS, state.getViewState());
        assertEquals(LOGIN, state.getCredentials().getLogin());
        assertEquals(PASSWORD, state.getCredentials().getPassword());
        assertEquals(requestCaptor.getValue(), state.getRequest());
        assertNull(state.getError());
    }

    @Test
    public void saveLoginState() {
        String errorMessage = "error";
        CommonError error = new CommonError(ErrorType.LOGIN, errorMessage);
        listenerCaptor.getValue().onFailure(error);

        ILoginPresenterState state = new MemoryLoginPresenterState();
        presenter.onSaveInstanceState(state);

        assertEquals(LoginViewState.ENTER_CREDENTIAL, state.getViewState());
        assertEquals(LOGIN, state.getCredentials().getLogin());
        assertEquals(PASSWORD, state.getCredentials().getPassword());
        assertNull(state.getRequest());
        assertEquals(error, state.getError());
    }

    @Test
    public void doNotSaveGenericError() {
        String errorMessage = "error";
        CommonError error = new CommonError(ErrorType.NETWORK, errorMessage);
        listenerCaptor.getValue().onFailure(error);

        ILoginPresenterState state = new MemoryLoginPresenterState();
        presenter.onSaveInstanceState(state);

        assertEquals(LoginViewState.ENTER_CREDENTIAL, state.getViewState());
        assertEquals(LOGIN, state.getCredentials().getLogin());
        assertEquals(PASSWORD, state.getCredentials().getPassword());
        assertNull(state.getRequest());
        assertNull(state.getError());
    }

}
