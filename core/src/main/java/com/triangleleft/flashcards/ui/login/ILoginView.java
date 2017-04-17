package com.triangleleft.flashcards.ui.login;

import com.triangleleft.flashcards.ui.common.view.IView;

import io.reactivex.Observable;

/**
 * View interface for login screen.
 */
public interface ILoginView extends IView {

    void render(LoginViewState viewState);

    Observable<String> logins();

    Observable<String> passwords();

    Observable<Boolean> rememberUserChecks();

    Observable<LoginEvent> loginEvents();
}
