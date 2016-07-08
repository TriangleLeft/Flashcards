package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(StubLoginModule.class);

    @Inject
    public StubLoginModule() {
    }

    @Override
    public Observable<Void> login(String login, String password) {
        return Observable.empty();
    }
}
