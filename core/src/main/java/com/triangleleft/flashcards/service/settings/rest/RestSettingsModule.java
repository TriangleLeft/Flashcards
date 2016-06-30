package com.triangleleft.flashcards.service.settings.rest;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements ISettingsModule {

    private final AccountModule accountModule;
    private final IDuolingoRest service;
    private UserData userData;

    @Inject
    public RestSettingsModule(AccountModule accountModule, IDuolingoRest service) {
        this.accountModule = accountModule;
        this.service = service;
    }

    @Override
    public UserData getCurrentUserData() {
        return userData;
    }

    @Override
    public Observable<UserData> getUserData() {
        Preconditions.checkNotNull(accountModule.getUserId());
        return service.getUserData(accountModule.getUserId())
                .map(UserDataModel::toUserData)
                .doOnNext(this::cacheData);
    }

    private void cacheData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public Observable<Void> switchLanguage(ILanguage language) {
        // We don't care about return result
        return service.switchLanguage(language.getId()).map(data -> null);
    }
}
