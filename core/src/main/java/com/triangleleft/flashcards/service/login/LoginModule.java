package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface LoginModule {

    void login(String login, String password, Observer<Void> observer);
}
