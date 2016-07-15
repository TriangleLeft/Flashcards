package com.triangleleft.flashcards.service.login.rest;

import android.support.annotation.Nullable;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.common.exception.ConversionException;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.PersistentStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginModule.class);

    private final IDuolingoRest service;
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;
    private final PersistentStorage storage;

    @Inject
    public RestLoginModule(IDuolingoRest service, SettingsModule settingsModule, AccountModule accountModule,
                           PersistentStorage storage) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.accountModule = accountModule;
        this.storage = storage;
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
        } else if (error instanceof ConversionException) {
            return Observable.error(error);
        } else if (error instanceof IOException) {
            return Observable.error(new NetworkException());
        } else {
            return Observable.error(error);
        }
    }

    private Observable<Void> processModel(LoginResponseModel model) {
        if (model.isSuccess()) {
            setUserId(model.userId);
            return settingsModule.loadUserData()
                    .map(data -> null);
        } else {
            return Observable.error(model.buildError());
        }
    }

    private void setUserId(@Nullable String userId) {
        accountModule.setUserId(userId);
    }

}