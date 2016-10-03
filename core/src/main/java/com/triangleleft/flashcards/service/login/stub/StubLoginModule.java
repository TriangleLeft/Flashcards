package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.Call;
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
    public Call<Object> login(String login, String password) {
        return new Call<Object>() {
            @Override
            public void enqueue(Action<Object> onData, Action<Throwable> onError) {
                if (!LOGIN.equals(login)) {
                    onError.call(new LoginException());
                } else if (!PASSWORD.equals(password)) {
                    onError.call(new PasswordException());
                } else {
                    onData.call(new Object());
                }
            }

            @Override
            public void cancel() {

            }
        };
    }
}
