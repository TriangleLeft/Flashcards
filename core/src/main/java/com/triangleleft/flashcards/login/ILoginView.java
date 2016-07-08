package com.triangleleft.flashcards.login;

import android.support.annotation.Nullable;

import com.triangleleft.flashcards.mvp.common.view.IView;

/**
 * View interface for login screen.
 */
public interface ILoginView extends IView {

    void setLoginButtonEnabled(boolean enabled);

    void setLogin(@Nullable String login);

    void setPassword(@Nullable String password);

    /**
     * Set login field error.
     *
     * @param error error to show
     */
    void setLoginError(@Nullable String error);

    /**
     * Set error field error.
     *
     * @param error error to show
     */
    void setPasswordError(@Nullable String error);

    void showProgress();

    void showContent();

    /**
     * Advance to next screen.
     */
    void advance();

    void setRememberUser(boolean rememberUser);

    void showGenericError();

    void showNetworkError();
}
