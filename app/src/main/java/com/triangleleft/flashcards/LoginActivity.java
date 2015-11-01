package com.triangleleft.flashcards;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.IloginListener;
import com.triangleleft.flashcards.service.error.CommonError;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
public class LoginActivity extends BaseActivity {

    private static final String KEY_STATE = "key_state";
    private static final String TAG = LoginActivity.class.getSimpleName();
    @Inject
    ILoginModule loginModule;
    @Bind(R.id.login)
    TextView loginView;
    @Bind(R.id.password)
    TextView passwordView;
    @Bind(R.id.view_flipper)
    ViewFlipper flipperView;

    IloginListener loginListener = new IloginListener() {
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

    ;
    private LoginState state;

    private void setState(@NonNull LoginState state) {
        this.state = state;
        switch (state) {
            case INPUT:
                flipperView.setDisplayedChild(1);
                break;
            case CHECK:
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


        if (loginModule.isLogged()) {
            // If we are logged, open main screen
            advanceToMainScreen();
        } else {
            // Check saved state
            if (savedInstanceState != null && savedInstanceState.getString(KEY_STATE) != null) {
                setState(LoginState.valueOf(savedInstanceState.getString(KEY_STATE)));
            } else {
                setState(LoginState.INPUT);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_STATE, state.name());
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
        loginModule.registerListener(loginListener);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        loginModule.unregisterListener(loginListener);
    }

    @OnClick(R.id.login_button)
    public void onLoginClick() {
        setState(LoginState.CHECK);
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        loginModule.login(login, password);
    }

    private enum LoginState {
        INPUT,
        CHECK
    }

}

