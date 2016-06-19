package com.triangleleft.flashcards.service.settings.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements ISettingsModule {

    private final LoginModule loginModule;
    private final IDuolingoRest service;
    private UserData cachedData;

    @Inject
    public RestSettingsModule(LoginModule loginModule, IDuolingoRest service) {
        this.loginModule = loginModule;
        this.service = service;
    }

    @Override
    public Observable<UserData> getUserData() {
        return service.getUserData(loginModule.getUserId()).map(UserDataModel::toUserData);
    }

    @Override
    public Observable<Void> switchLanguage(ILanguage language) {
        // We don't care about return result
        return service.switchLanguage(language.getId()).map(data -> null);
    }
}
