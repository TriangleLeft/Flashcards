package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public interface LoginModule {

    Observable<Object> login(String login, String password);
}
