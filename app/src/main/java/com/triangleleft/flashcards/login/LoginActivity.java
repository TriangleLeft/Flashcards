package com.triangleleft.flashcards.login;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.BaseActivity;
import com.triangleleft.flashcards.Injector;
import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.login.presenter.ILoginPresenter;
import com.triangleleft.flashcards.service.ICommonListener;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.login.view.ILoginView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
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

    private static final String KEY_STATE = "key_state";
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    ILoginPresenter presenter;
    @Bind(R.id.login)
    TextView loginView;
    @Bind(R.id.password)
    TextView passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;

    ICommonListener loginListener = new ICommonListener() {
        @Override
        public void onSuccess() {
            advanceToMainScreen();
        }

        @Override
        public void onError(@NonNull CommonError error) {
            setState(LoginState.INPUT);
            handleError(error);
        }
    };

    private void handleError(@NonNull CommonError error) {
        Toast.makeText(LoginActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void setState(@NonNull LoginViewState state) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Injector.INSTANCE.getComponent().inject(this);

        presenter.onBind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    private void advanceToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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

    }

    @Override
    public void setPasswordError(@Nullable String error) {

    }

    @OnClick(R.id.login_button)
    protected void onLoginClick() {
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        presenter.onLoginClick(login, password);
    }


}

