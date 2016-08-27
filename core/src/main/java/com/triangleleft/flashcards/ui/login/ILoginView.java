package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.ui.common.view.IView;

import android.support.annotation.Nullable;

/**
 * View interface for login screen.
 */
public interface ILoginView extends IView {

    void setLoginButtonEnabled(boolean enabled);

    void setLogin(@Nullable String login);

    void setPassword(@Nullable String password);

    void setLoginErrorVisible(boolean visible);

    void setPasswordErrorVisible(boolean visible);

    void showProgress();

    void showContent();

    /**
     * Advance to next screen.
     */
    void advance();

    void setRememberUser(boolean rememberUser);

    void notifyGenericError();

    void notifyNetworkError();
}
