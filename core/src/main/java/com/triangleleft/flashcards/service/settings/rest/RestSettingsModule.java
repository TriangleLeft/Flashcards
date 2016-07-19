package com.triangleleft.flashcards.service.settings.rest;

import com.google.common.base.Preconditions;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.PersistentStorage;
import rx.Observable;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements SettingsModule {

    private final AccountModule accountModule;
    private final RestService service;
    private final PersistentStorage storage;

    @Inject
    public RestSettingsModule(AccountModule accountModule, RestService service, PersistentStorage storage) {
        this.accountModule = accountModule;
        this.service = service;
        this.storage = storage;
    }

    @Override
    public Observable<UserData> loadUserData() {
        Preconditions.checkState(accountModule.getUserId().isPresent());
        return service.getUserData(accountModule.getUserId().get())
                .map(UserDataModel::toUserData)
                .doOnNext(accountModule::setUserData);
    }

    @Override
    public Observable<Void> switchLanguage(Language language) {
        // We don't care about return result
        return service.switchLanguage(language.getId()).map(data -> null);
    }
}
