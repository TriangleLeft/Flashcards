package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.exception.LoginException;
import com.triangleleft.flashcards.service.login.exception.PasswordException;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public class StubLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(StubLoginModule.class);
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;

    @Inject
    public StubLoginModule(SettingsModule settingsModule, AccountModule accountModule) {
        this.settingsModule = settingsModule;
        this.accountModule = accountModule;
    }

    @Override
    public Observable<Object> login(String login, String password) {
        if (!LOGIN.equals(login)) {
            return Observable.error(new LoginException());
        } else if (!PASSWORD.equals(password)) {
            return Observable.error(new PasswordException());
        } else {
            UserData userData = settingsModule.loadUserData().blockingFirst();
            accountModule.setUserData(userData);
            accountModule.setLogin(login);
            accountModule.setUserId("fake");

            return Observable.just(new Object());
        }
    }
}
