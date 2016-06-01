package com.triangleleft.flashcards.service.settings.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestSettingsModule implements ISettingsModule {

    private final IDuolingoRest service;

    public RestSettingsModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public Observable<IUserData> getUserData() {
        return Observable.empty();
    }

    @Override
    public Observable<Void> switchLanguage(ILanguage language) {
        return Observable.empty();
    }
}
