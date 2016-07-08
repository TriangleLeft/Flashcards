package com.triangleleft.flashcards.service.settings.rest;

import com.google.common.base.Preconditions;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.UserDataModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.PersistentStorage;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements SettingsModule {

    private final static String KEY_USERDATA = "RestSettingsModule::userData";
    private final AccountModule accountModule;
    private final IDuolingoRest service;
    private final PersistentStorage storage;
    private UserData userData;

    @Inject
    public RestSettingsModule(AccountModule accountModule, IDuolingoRest service, PersistentStorage storage) {
        this.accountModule = accountModule;
        this.service = service;
        this.storage = storage;
        this.userData = storage.get(KEY_USERDATA, UserData.class, null);
    }

    @Override
    public UserData getCurrentUserData() {
        return userData;
    }

    @Override
    public Observable<UserData> getUserData() {
        Preconditions.checkState(accountModule.getUserId().isPresent());
        return service.getUserData(accountModule.getUserId().get())
                .map(UserDataModel::toUserData)
                .doOnNext(this::cacheData);
    }

    private void cacheData(UserData userData) {
        this.userData = userData;
        storage.put(KEY_USERDATA, userData);
    }

    @Override
    public Observable<Void> switchLanguage(Language language) {
        // We don't care about return result
        return service.switchLanguage(language.getId()).map(data -> null);
    }
}
