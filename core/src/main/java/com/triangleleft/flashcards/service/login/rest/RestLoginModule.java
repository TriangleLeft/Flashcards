package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class RestLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginModule.class);

    private final RestService service;
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;

    @Inject
    public RestLoginModule(RestService service, SettingsModule settingsModule, AccountModule accountModule) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.accountModule = accountModule;
    }

    @Override
    public void login(String login, String password, Observer<Void> observer) {
        accountModule.setLogin(login);
        service.login(new LoginRequestController(login, password))
                .enqueue(model -> {
                    if (model.isSuccess()) {
                        accountModule.setUserId(model.getUserId());
                        settingsModule.loadUserData(new Observer<UserData>() {
                            @Override
                            public void onError(Throwable e) {
                                observer.onError(e);
                            }

                            @Override
                            public void onNext(UserData data) {
                                accountModule.setUserData(data);
                                observer.onNext(null);
                            }
                        });
                    } else {
                        observer.onError(model.getError());
                    }
                }, observer::onError);
    }


}