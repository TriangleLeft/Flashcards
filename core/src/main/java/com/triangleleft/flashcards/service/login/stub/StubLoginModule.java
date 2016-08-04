package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

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
    public Observable<Void> login(String login, String password) {
        if (!LOGIN.equals(login)) {
            return Observable.error(new LoginException());
        } else if (!PASSWORD.equals(password)) {
            return Observable.error(new PasswordException());
        } else {
            return Observable.just(null);
        }
    }
}
