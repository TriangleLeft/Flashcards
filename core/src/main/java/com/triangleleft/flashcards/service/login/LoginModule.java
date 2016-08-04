package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface LoginModule {

    Observable<Void> login(String login, String password);
}
