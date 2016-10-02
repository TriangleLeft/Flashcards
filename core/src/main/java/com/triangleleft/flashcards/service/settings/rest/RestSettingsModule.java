package com.triangleleft.flashcards.service.settings.rest;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.settings.rest.model.SwitchLanguageController;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import javax.inject.Inject;

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
    public void loadUserData(Observer<UserData> observer) {
        service.getUserData(accountModule.getUserId().get())
                .enqueue(model -> {
                    UserData data = model.toUserData();
                    observer.onNext(data);
                }, observer::onError);
    }

    @Override
    public void switchLanguage(Language language, Observer<Void> observer) {
        service.switchLanguage(new SwitchLanguageController(language.getId()))
                .enqueue(data -> observer.onNext(null), observer::onError);
    }

}
