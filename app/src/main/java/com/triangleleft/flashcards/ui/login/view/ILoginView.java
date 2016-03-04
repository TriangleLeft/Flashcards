package com.triangleleft.flashcards.ui.login.view;

import com.triangleleft.flashcards.service.Credentials;
import com.triangleleft.flashcards.ui.common.view.IView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * View interface for login screen.
 */
public interface ILoginView extends IView {

    void setLoginButtonEnabled(boolean enabled);

    void setCredentials(@NonNull Credentials credentials);

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

    void setGenericError(@Nullable String error);

    /**
     * Set view state.
     * @param state new state
     */
    void setState(@NonNull LoginViewState state);

    /**
     * Advance to next screen.
     */
    void advance();

}
