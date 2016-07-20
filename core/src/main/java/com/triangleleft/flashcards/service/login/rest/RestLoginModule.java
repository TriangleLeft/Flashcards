package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

import java.io.IOException;
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
    public Observable<Void> login(String login, String password) {
        accountModule.setLogin(login);
        return service.login(login, password)
                .flatMap(this::processModel)
                .onErrorResumeNext(this::processError);
    }

    private Observable<Void> processError(Throwable error) {
        logger.debug("processError called with [{}]", error);
        if (error instanceof HttpException) {
            return Observable.error(new ServerException());
        } else if (error instanceof IOException) {
            return Observable.error(new NetworkException());
        } else {
            return Observable.error(error);
        }
    }

    private Observable<Void> processModel(LoginResponseModel model) {
        if (model.isSuccess()) {
            accountModule.setUserId(model.getUserId());
            return settingsModule.loadUserData()
                    .map(data -> null);
        } else {
            return Observable.error(model.getError());
        }
    }

}