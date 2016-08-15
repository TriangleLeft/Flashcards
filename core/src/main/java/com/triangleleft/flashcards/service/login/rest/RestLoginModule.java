package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;

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
    public Observable<Void> login(String login, String password) {
        accountModule.setLogin(login);
        return service.login(login, password)
                .flatMap(this::processModel);
    }

    private Observable<Void> processModel(LoginResponseModel model) {
        logger.debug("processModel called with [{}]", model);
        if (model.isSuccess()) {
            accountModule.setUserId(model.getUserId());
            return settingsModule.loadUserData()
                    .map(data -> null);
        } else {
            return Observable.error(model.getError());
        }
    }

}