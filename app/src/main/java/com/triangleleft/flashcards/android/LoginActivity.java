package com.triangleleft.flashcards.android;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.dagger.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.dagger.LoginActivityModule;
import com.triangleleft.flashcards.service.Credentials;
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
                AssertDialog.fail("Unknown state " + state.name());
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        DaggerLoginActivityComponent.builder().applicationComponent(getApplicationComponent())
                .loginActivityModule(new LoginActivityModule()).build().inject(this);

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logger.debug("onSaveInstanceState() called with: outState = [{}]", outState);
        super.onSaveInstanceState(outState);

        //presenter.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        logger.debug("onRestoreInstanceState() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        //presenter.onRestoreInstanceState(savedInstanceState);
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

