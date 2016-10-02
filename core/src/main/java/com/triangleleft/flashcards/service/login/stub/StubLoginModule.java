package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class StubLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(StubLoginModule.class);
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";

    @Inject
    public StubLoginModule() {
    }

    @Override
    public void login(String login, String password, Observer<Void> observer) {
        if (!LOGIN.equals(login)) {
            observer.onError(new LoginException());
        } else if (!PASSWORD.equals(password)) {
            observer.onError(new PasswordException());
        } else {
            observer.onNext(null);
        }
    }
}
