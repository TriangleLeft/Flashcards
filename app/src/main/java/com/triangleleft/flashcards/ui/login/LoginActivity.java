package com.triangleleft.flashcards.ui.login;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.dagger.DaggerLoginActivityComponent;
import com.triangleleft.flashcards.dagger.LoginActivityComponent;
import com.triangleleft.flashcards.dagger.LoginActivityModule;
import com.triangleleft.flashcards.ui.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.ui.login.view.ILoginView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.ViewFlipper;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    ILoginPresenter presenter;
    @Bind(R.id.login)
    TextView loginView;
    @Bind(R.id.password)
    TextView passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;
    private LoginActivityComponent component;

    @Override
    public void setState(@NonNull LoginViewState state) {
        Log.d(TAG, "setState() called with: " + "state = [" + state + "]");
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
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        component = DaggerLoginActivityComponent.builder().applicationComponent(getApplicationComponent())
                .loginActivityModule(new LoginActivityModule()).build();
        component.inject(this);


        presenter.onBind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = [" + outState + "]");
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void setLoginError(@Nullable String error) {
        Log.d(TAG, "setLoginError() called with: " + "error = [" + error + "]");
        loginView.setError(error);
    }

    @Override
    public void setPasswordError(@Nullable String error) {
        Log.d(TAG, "setPasswordError() called with: " + "error = [" + error + "]");
        passwordView.setError(error);
    }

    @OnClick(R.id.login_button)
    protected void onLoginClick() {
        Log.d(TAG, "onLoginClick()");
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        presenter.onLoginClick(login, password);
    }

    @Override
    public void advance() {
        Log.d(TAG, "advance()");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

