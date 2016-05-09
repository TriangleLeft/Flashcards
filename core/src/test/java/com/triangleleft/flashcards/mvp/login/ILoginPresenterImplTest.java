package com.triangleleft.flashcards.mvp.login;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginResult;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private LoginPresenter presenter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(module);
        presenter.onRebind(view);
    }

//    @Test
//    public void loginErrorClearedOnNewLogin() {
//        presenter.onLoginChanged("value");
//        verify(view).setLoginError(isNull(String.class));
//    }
//
//    @Test
//    public void passwordErrorClearedOnNewPassword() {
//        presenter.onPasswordChanged("value");
//        verify(view).setPasswordError(isNull(String.class));
//    }
//
//    @Test
//    public void restoreRequest() {
//        ILoginPresenterState state = new com.triangleleft.flashcards.mvp.login.presenter.MemoryLoginPresenterState();
//        ILoginRequest request = new SimpleLoginRequest(new Credentials("login", "password"));
//        state.setRequest(request);
//        state.setViewState(LoginViewStatePage.PROGRESS);
//        presenter.onRestoreInstanceState(state);
//        presenter.onResume();
//        verify(module).registerListener(eq(request), any());
//    }
//
//    @Test
//    public void restoreError() {
//        ILoginPresenterState state = new com.triangleleft.flashcards.mvp.login.presenter.MemoryLoginPresenterState();
//        Credentials credentials = new Credentials("login", "password");
//        CommonError error = new CommonError(ErrorType.LOGIN, "message");
//        state.setViewState(LoginViewStatePage.ENTER_CREDENTIAL);
//        state.setCredentials(credentials);
//        state.setError(error);
//        presenter.onRestoreInstanceState(state);
//        presenter.onResume();
//        verify(view).setCredentials(credentials);
//        verify(view).setLoginError(error.getMessage());
//    }

}
