package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.util.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoginViewActions {

    private final static Logger logger = LoggerFactory.getLogger(LoginViewActions.class.getSimpleName());

    public static ViewAction<LoginViewState> setLogin(final String login) {
        logger.debug("setLogin {}", login);
        return state -> state.asBuilder()
                .login(login)
                .loginButtonEnabled(isLoginButtonEnabled(login, state.password()))
                .build();
    }

    public static ViewAction<LoginViewState> setPassword(final String password) {
        logger.debug("setPassword {}", password);
        return state -> state.asBuilder()
                .password(password)
                .loginButtonEnabled(isLoginButtonEnabled(state.login(), password))
                .build();
    }

    private static boolean isLoginButtonEnabled(String login, String password) {
        logger.debug("isLoginButtonEnabled: {} {}", login, password);
        return TextUtils.hasText(login) && TextUtils.hasText(password);
    }

    public static ViewAction<LoginViewState> setRememberUser(boolean rememberUser) {
        return state -> state.asBuilder().shouldRememberUser(rememberUser).build();
    }

    public static ViewAction<LoginViewState> loginError() {
        return state -> state.asBuilder().page(LoginViewState.Page.CONTENT)
                .hasLoginError(true)
                .build();
    }

    public static ViewAction<LoginViewState> passwordError() {
        return state -> state.asBuilder().page(LoginViewState.Page.CONTENT)
                .hasPasswordError(true).build();
    }

    public static ViewAction<LoginViewState> genericError(final String genericError) {
        return state -> state.asBuilder().page(LoginViewState.Page.CONTENT).genericError(genericError)
                .build();
    }

    public static ViewAction<LoginViewState> success() {
        return state -> state.asBuilder().page(LoginViewState.Page.SUCCESS).build();
    }

    public static ViewAction<LoginViewState> progress() {
        return state -> state.asBuilder().page(LoginViewState.Page.PROGRESS).build();
    }
}
