package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.SystemOutTree;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.SimpleLoginRequest;
import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginResult;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class ILoginPresenterImplTest {

    @Mock
    ILoginModule module;

    @Mock
    ILoginView view;

    @Mock
    ILoginResult loginResult;

    @Captor
    ArgumentCaptor<IListener<ILoginResult>> listenerCaptor;

    private ILoginPresenterImpl presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new ILoginPresenterImpl(module);
        presenter.onBind(view);
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        Timber.plant(new SystemOutTree());
    }

    @Test
    public void loginErrorClearedOnNewLogin() {
        presenter.onLoginChanged("value");
        verify(view).setLoginError(isNull(String.class));
    }

    @Test
    public void passwordErrorClearedOnNewPassword() {
        presenter.onPasswordChanged("value");
        verify(view).setPasswordError(isNull(String.class));
    }

    @Test
    public void restoreRequest() {
        ILoginPresenterState state = new MemoryLoginPresenterState();
        ILoginRequest request = new SimpleLoginRequest(new Credentials("login", "password"));
        state.setRequest(request);
        state.setViewState(LoginViewState.PROGRESS);
        presenter.onRestoreInstanceState(state);
        presenter.onResume();
        verify(module).registerListener(eq(request), any());
    }

    @Test
    public void restoreError() {
        ILoginPresenterState state = new MemoryLoginPresenterState();
        Credentials credentials = new Credentials("login", "password");
        CommonError error = new CommonError(ErrorType.LOGIN, "message");
        state.setViewState(LoginViewState.ENTER_CREDENTIAL);
        state.setCredentials(credentials);
        state.setError(error);
        presenter.onRestoreInstanceState(state);
        presenter.onResume();
        verify(view).setCredentials(credentials);
        verify(view).setLoginError(error.getMessage());
    }

}
