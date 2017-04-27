package com.triangleleft.flashcards.service.settings.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.SwitchLanguageController;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import javax.inject.Inject;

import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements SettingsModule {

    private final AccountModule accountModule;
    private final RestService service;

    @Inject
    public RestSettingsModule(RestService service, AccountModule accountModule) {
        this.service = service;
        this.accountModule = accountModule;
    }

    @Override
    public Observable<UserData> loadUserData() {
        return service.getUserData(accountModule.getUserId().get())
                .map(UserDataModel::toUserData)
                .doOnNext(accountModule::setUserData);
    }

    @Override
    public Observable<Object> switchLanguage(Language language) {
        // We don't care about actual return result
        return service.switchLanguage(new SwitchLanguageController(language.getId()))
                .map(data -> null);
    }

}
