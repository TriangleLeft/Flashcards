package com.triangleleft.flashcards.ui.login.presenter;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.service.Credentials;
import com.triangleleft.flashcards.service.IListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.ILoginRequest;
import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.LoginStatus;
import com.triangleleft.flashcards.service.SimpleLoginRequest;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;
import com.triangleleft.flashcards.ui.login.view.LoginViewState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkState;

public class ILoginPresenterImpl extends AbstractPresenter<ILoginView, ILoginPresenterState>
        implements ILoginPresenter {

    private static final Logger logger = LoggerFactory.getLogger(ILoginPresenterImpl.class);

    private LoginViewState currentState = LoginViewState.ENTER_CREDENTIAL;
    private final ILoginModule loginModule;
    private CommonError error;
    private ILoginRequest loginRequest;

    private IListener<ILoginResult> loginListener = new LoginListener();
    private Credentials credentials = new Credentials();

    @Inject
    public ILoginPresenterImpl(@NonNull ILoginModule loginModule) {
        logger.debug("ILoginPresenterImpl() called with: loginModule = [{}]", loginModule);
        this.loginModule = loginModule;
    }

    @Override
    public void onLoginChanged(@NonNull String login) {
        credentials.setLogin(login);
        // TODO: really looks like a task for ViewModel
        // clear error
        error = null;
        // update view
        getView().setLoginError(null);
    }

    @Override
    public void onPasswordChanged(@NonNull String password) {
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
    public void onPause() {
        logger.debug("onPause() called");
        if (loginRequest != null) {
            loginModule.unregisterListener(loginRequest, loginListener);
        }
    }

    @Override
    public void onResume() {
        logger.debug("onResume() called");
        if (loginRequest != null) {
            loginModule.registerListener(loginRequest, loginListener);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull ILoginPresenterState outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        outState.setCredentials(credentials);
        outState.setRequest(loginRequest);
        outState.setViewState(currentState);
        outState.setError(error);
    }

    @Override
    public void onRestoreInstanceState(@Nullable ILoginPresenterState inState) {
        logger.debug("onRestoreInstanceState() called with: inState = [{}]", inState);
        if (inState != null) {
            credentials = inState.getCredentials();
            loginRequest = inState.getRequest();
            LoginViewState state = inState.getViewState();
            setCurrentState(state);
            switch (state) {
                case ENTER_CREDENTIAL:
                    getView().setCredentials(credentials);
                    CommonError error = inState.getError();
                    if (error != null) {
                        handleError(error);
                    }
                    break;
                case PROGRESS:
                    // We were waiting for request to complete
                    // Do nothing, listener is guaranteed to be called
                    break;
                default:
                    AssertDialog.fail("Unknown state: " + state);
            }
        }
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
            checkState(result.getResult() == LoginStatus.LOGGED, "Got unknown status: " + result.getResult());
            // Advance to next screen
            getView().advance();
        }

        @Override
        public void onFailure(@NonNull CommonError error) {
            logger.debug("onFailure() called with: error = [{}]", error);
            handleError(error);
        }
    }

}
