package com.triangleleft.flashcards.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.BaseActivity;
import com.triangleleft.flashcards.common.CustomEditText;
import com.triangleleft.flashcards.main.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginActivityComponent, ILoginView, LoginPresenter>
        implements ILoginView {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private static final int CONTENT = 0;
    private static final int PROGRESS = 1;

    @Bind(R.id.login_email)
    CustomEditText loginView;
    @Bind(R.id.login_password)
    CustomEditText passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.login_checkbox)
    CheckBox checkBox;

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
    public void setLoginErrorVisible(boolean visible) {
        if (visible) {
            loginView.setError(getString(R.string.wrong_login));
        } else {
            loginView.setError(null);
        }
    }

    @Override
    public void setPasswordErrorVisible(boolean visible) {
        if (visible) {
            passwordView.setError(getString(R.string.wrong_password));
        } else {
            passwordView.setError(null);
        }
    }

    @OnClick(R.id.login_checkbox)
    public void onCheckboxClick() {
        getPresenter().onRememberCheck(checkBox.isChecked());
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

    @Override
    public void setRememberUser(boolean rememberUser) {
        checkBox.setChecked(rememberUser);
    }

    @Override
    public void showProgress() {
        flipperView.setDisplayedChild(PROGRESS);
    }

    @Override
    public void showContent() {
        flipperView.setDisplayedChild(CONTENT);
        if (loginView.getError() != null) {
            loginView.requestFocus();
        } else if (passwordView.getError() != null) {
            passwordView.requestFocus();
        }
    }

    @Override
    public void showGenericError() {
        Toast.makeText(this, R.string.login_generic_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.login_network_error, Toast.LENGTH_SHORT).show();
    }
}

