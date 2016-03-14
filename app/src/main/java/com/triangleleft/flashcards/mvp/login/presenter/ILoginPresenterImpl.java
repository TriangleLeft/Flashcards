package com.triangleleft.flashcards.mvp.login.presenter;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.login.view.ILoginView;
import com.triangleleft.flashcards.mvp.login.view.ILoginViewDelegate;
import com.triangleleft.flashcards.mvp.login.view.LoginViewState;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.login.SimpleLoginRequest;
import com.triangleleft.flashcards.service.provider.IListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkState;

public class ILoginPresenterImpl extends AbstractPresenter<ILoginView>
        implements ILoginPresenter {

    private static final Logger logger = LoggerFactory.getLogger(ILoginPresenterImpl.class);

    private LoginViewState currentState = LoginViewState.ENTER_CREDENTIAL;
    private final ILoginModule loginModule;
    private CommonError error;
    private ILoginRequest loginRequest;

    private IListener<ILoginResult> loginListener = new LoginListener();
    private Credentials credentials = new Credentials();

    public ILoginPresenterImpl(@NonNull ILoginModule loginModule, @NonNull ILoginViewDelegate viewDelegate) {
        super(viewDelegate, viewDelegate);
        logger.debug("ILoginPresenterImpl() called with: loginModule = [{}]", loginModule);
        this.loginModule = loginModule;

        // If we are already logged, advance immediately.
        if (loginModule.getLoginStatus() == LoginStatus.LOGGED) {
            getView().advance();
        }
    }

    @Override
    public void onLoginChanged(@NonNull String login) {
        logger.debug("onLoginChanged() called with: login = [{}]", login);
        credentials.setLogin(login);
        // TODO: really looks like a task for ViewModel
        // clear error
        error = null;
        // update view
        getView().setLoginError(null);
    }

    @Override
    public void onPasswordChanged(@NonNull String password) {
        logger.debug("onPasswordChanged() called with: password = [{}]", password);
        credentials.setPassword(password);
        // clear error
        error = null;
        // update view
        getView().setPasswordError(null);
    }

    @Override
    public void onLoginClick() {
        logger.debug("onLoginClick() called");
        setCurrentState(LoginViewState.PROGRESS);
        checkState(loginRequest == null, "Doing login request while another one is pending");
        loginRequest = new SimpleLoginRequest(credentials);
        loginModule.doRequest(loginRequest, loginListener);
    }

    @Override
    public void onBind(@NonNull ILoginView view) {
        super.onBind(view);
        //  setCurrentState(LoginViewState.ENTER_CREDENTIAL);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
        loginModule.cancelRequest(loginRequest);
        loginRequest = null;
    }

    private void handleError(@NonNull CommonError error) {
        this.error = error;
        setCurrentState(LoginViewState.ENTER_CREDENTIAL);
        // TODO: probably we don't want to rely on messages here and use predefined strings instead
        switch (error.getType()) {
            case LOGIN:
                getView().setLoginError(error.getMessage());
                break;
            case PASSWORD:
                getView().setPasswordError(error.getMessage());
                break;
            default:
                getView().setGenericError(error.getMessage());
                // Don't save error
                this.error = null;
                // TODO: why?
                break;
        }

    }

    private void setCurrentState(@NonNull LoginViewState state) {
        logger.debug("setCurrentState() called with: state = [{}]", state);
        currentState = state;
        getView().setState(state);
    }

    private class LoginListener implements IListener<ILoginResult> {

        @Override
        public void onResult(@NonNull ILoginResult result) {
            logger.debug("onResult() called with: result = [{}]", result);
            loginRequest = null;

            checkState(result.getResult() == LoginStatus.LOGGED, "Got unknown status: " + result.getResult());
            // Advance to next screen
            getView().advance();
        }

        @Override
        public void onFailure(@NonNull CommonError error) {
            logger.debug("onFailure() called with: error = [{}]", error);
            loginRequest = null;
            handleError(error);
        }
    }

}
