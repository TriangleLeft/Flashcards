package com.triangleleft.flashcards.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.login.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.di.login.LoginActivityComponent;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.ui.main.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginActivityComponent, ILoginView, LoginViewState, LoginPresenter>
        implements ILoginView {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private static final int CONTENT = 0;
    private static final int PROGRESS = 1;

    @Bind(R.id.login_email)
    EditText loginView;
    @Bind(R.id.login_email_layout)
    TextInputLayout loginLayoutView;
    @Bind(R.id.login_password)
    EditText passwordView;
    @Bind(R.id.login_password_layout)
    TextInputLayout passwordLayoutView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.login_switch)
    SwitchCompat rememberSwitch;
    @Bind(R.id.login_container)
    View container;

    private LoginViewState.Page currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupUI(container);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected LoginActivityComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerLoginActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build();
    }

    @NonNull
    @Override
    protected ILoginView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @Override
    public void render(LoginViewState viewState) {
        logger.debug("render() called with {}", viewState);
        setLogin(viewState.login());
        setPassword(viewState.password());
        setLoginErrorVisible(viewState.hasLoginError());
        setPasswordErrorVisible(viewState.hasPasswordError());
        setRememberUser(viewState.shouldRememberUser());
        setLoginButtonEnabled(viewState.loginButtonEnabled());
        if (currentPage != viewState.page()) {
            currentPage = viewState.page();
            switch (viewState.page()) {
                case PROGRESS:
                    showProgress();
                    break;
                case CONTENT:
                    showContent();
                    break;
                case SUCCESS:
                    advance();
                    break;
            }
        }
    }

    private void setLoginButtonEnabled(boolean enabled) {
        logger.debug("setLoginButtonEnabled() called with: enabled = [{}]", enabled);
        loginButton.setEnabled(enabled);
        if (enabled) {
            loginButton.getBackground().setColorFilter(null);
        } else {
            loginButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setLogin(@Nullable String login) {
        logger.debug("setLogin() called with: login = [{}]", login);
        if (!loginView.getText().toString().equals(login)) {
            loginView.setText(login);
        }
    }

    private void setPassword(@Nullable String password) {
        logger.debug("setPassword() called with: password = [{}]", password);
        if (!passwordView.getText().toString().equals(password)) {
            passwordView.setText(password);
        }
    }

    private void setLoginErrorVisible(boolean visible) {
        loginLayoutView.setError(visible ? getString(R.string.wrong_login) : null);
    }

    private void setPasswordErrorVisible(boolean visible) {
        passwordLayoutView.setError(visible ? getString(R.string.wrong_password) : null);
    }

    private void advance() {
        logger.debug("advance() called");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void setRememberUser(boolean rememberUser) {
        rememberSwitch.setChecked(rememberUser);
    }

    private void showProgress() {
        flipperView.setDisplayedChild(PROGRESS);
    }

    private void showContent() {
        flipperView.setDisplayedChild(CONTENT);
        if (loginView.getError() != null) {
            loginView.requestFocus();
        } else if (passwordView.getError() != null) {
            passwordView.requestFocus();
        }
    }

    @Override
    public Observable<String> logins() {
        return RxTextView.textChanges(loginView).map(String::valueOf);
    }

    @Override
    public Observable<String> passwords() {
        return RxTextView.textChanges(passwordView).map(String::valueOf);
    }

    @Override
    public Observable<Boolean> rememberUserChecks() {
        return RxCompoundButton.checkedChanges(rememberSwitch);
    }

    @Override
    public Observable<LoginEvent> loginEvents() {
        return RxView.clicks(loginButton)
                .map(ignored -> LoginEvent.create(loginView.getText().toString(), passwordView.getText().toString()));
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        // We should also ignore "show password" icon
        if (!(view instanceof EditText) && !(view instanceof CheckableImageButton)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}

