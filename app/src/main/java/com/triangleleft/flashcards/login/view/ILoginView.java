package com.triangleleft.flashcards.login.view;

import com.triangleleft.flashcards.view.IView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * View interface for login screen.
 */
public interface ILoginView extends IView {
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

    /**
     * Set view state.
     * @param state new state
     */
    void setState(@NonNull LoginViewState state);

    /**
     * Current view state.
     */
    enum LoginViewState {
        ENTER_CREDENTIAL,
        PROGRESS
    }
}
