package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.flashcards.SystemOutTree;
import com.triangleleft.flashcards.service.IListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.ui.login.view.ILoginView;

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


}
