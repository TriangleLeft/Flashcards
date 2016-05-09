package com.triangleleft.flashcards.login;

import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.CustomEditText;
import com.triangleleft.flashcards.login.di.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.login.di.LoginActivityComponent;
import com.triangleleft.flashcards.main.MainActivity;
import com.triangleleft.flashcards.mvp.login.ILoginView;
import com.triangleleft.flashcards.mvp.login.LoginPresenter;
import com.triangleleft.flashcards.mvp.login.LoginViewStatePage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginActivityComponent, ILoginView, LoginPresenter>
        implements ILoginView {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    @Bind(R.id.login)
    CustomEditText loginView;
    @Bind(R.id.password)
    CustomEditText passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;
    @Bind(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginView.setOnTextChangedListener(newText -> getPresenter().onLoginChanged(newText));
        passwordView.setOnTextChangedListener(newText -> getPresenter().onPasswordChanged(newText));
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected LoginActivityComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerLoginActivityComponent.builder().applicationComponent(getApplicationComponent()).build();
    }

    @NonNull
    @Override
    protected ILoginView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @Override
    public void setState(@NonNull LoginViewStatePage state) {
        logger.debug("setState() called with: state = [{}]", state);
        switch (state) {
            case ENTER_CREDENTIAL:
                flipperView.setDisplayedChild(0);
                break;
            case PROGRESS:
                flipperView.setDisplayedChild(1);
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
    public void setLogin(@Nullable String login) {
        logger.debug("setLogin() called with: login = [{}]", login);
        loginView.replaceText(login);
    }

    @Override
    public void setPassword(@Nullable String password) {
        passwordView.replaceText(password);
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
        getPresenter().onLoginClick();
    }

    @Override
    public void advance() {
        logger.debug("advance() called");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}

