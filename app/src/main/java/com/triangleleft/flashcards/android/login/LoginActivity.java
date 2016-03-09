package com.triangleleft.flashcards.android.login;

import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.android.SimpleTextWatcher;
import com.triangleleft.flashcards.dagger.LoginActivityComponent;
import com.triangleleft.flashcards.service.login.Credentials;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;
import com.triangleleft.flashcards.ui.login.view.LoginViewState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    @Inject
    ILoginPresenter presenter;
    @Bind(R.id.login)
    EditText loginView;
    @Bind(R.id.password)
    EditText passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;
    @Bind(R.id.login_button)
    Button loginButton;
    private LoginActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        component = getApplicationComponent().getApplication().buildLoginActivityComponent();
        component.inject(this);

        loginView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onChanged() {
                presenter.onLoginChanged(loginView.getText().toString());
            }
        });
        passwordView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onChanged() {
                presenter.onPasswordChanged(passwordView.getText().toString());
            }
        });


        presenter.onBind(this);

        if (savedInstanceState != null) {
            presenter.onRestoreInstanceState(new BundleLoginPresenterState(savedInstanceState));
        } else {
            presenter.onCreateInstanceState();
        }
    }

    public LoginActivityComponent getComponent() {
        logger.debug("getComponent() called");
        return component;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(new BundleLoginPresenterState(outState));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        logger.debug("onRestoreInstanceState() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            presenter.onRestoreInstanceState(new BundleLoginPresenterState(savedInstanceState));
        }
    }

    @Override
    protected void onResume() {
        logger.debug("onResume() called");
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        logger.debug("onPause() called");
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void setState(@NonNull LoginViewState state) {
        logger.debug("setState() called with: state = [{}]", state);
        switch (state) {
            case ENTER_CREDENTIAL:
                flipperView.setDisplayedChild(1);
                break;
            case PROGRESS:
                flipperView.setDisplayedChild(0);
                break;
            default:
                throw new IllegalStateException("Unknown state " + state.name());
        }
    }

    @Override
    public void setLoginButtonEnabled(boolean enabled) {
        logger.debug("setLoginButtonEnabled() called with: enabled = [{}]", enabled);
        loginButton.setEnabled(enabled);
    }

    @Override
    public void setCredentials(@NonNull Credentials credentials) {

    }

    @Override
    public void setLoginError(@Nullable String error) {
        logger.debug("setLoginError() called with: error = [{}]", error);
        loginView.setError(error);
    }

    @Override
    public void setPasswordError(@Nullable String error) {
        logger.debug("setPasswordError() called with: error = [{}]", error);
        passwordView.setError(error);
    }

    @Override
    public void setGenericError(@Nullable String error) {
        logger.debug("setGenericError() called with: error = [{}]", error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_button)
    protected void onLoginClick() {
        logger.debug("onLoginClick() called");
        presenter.onLoginClick();
    }

    @Override
    public void advance() {
        logger.debug("advance() called");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

