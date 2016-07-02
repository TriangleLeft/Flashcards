package com.triangleleft.flashcards.mvp.login;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.mvp.common.view.Stateless;

import android.support.annotation.Nullable;

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

    @Stateless
    void setGenericError(@Nullable String error);

    /**
     * Set view state.
     * @param state new state
     */
    void setState(LoginViewStatePage state);

    /**
     * Advance to next screen.
     */
    void advance();

    void setRememberUser(boolean rememberUser);
}
