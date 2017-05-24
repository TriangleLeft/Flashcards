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
    public Observable<UserData> userData() {
        return service.getUserData(accountModule.getUserId().get())
                .map(UserDataModel::toUserData)
                .doOnNext(accountModule::setUserData)
                .startWith(accountModule.getUserData().get());
    }

    @Override
    public Observable<Object> switchLanguage(Language language) {

//        // We have successfully switch language
//        // Update our local userdata
//        val data = accountModule.userData.get()
//        // Mark language as "current learning"
//        val languages = data.languages
//                .map { language -> language.withCurrentLearning(language.id == currentLanguage.id) }
//        // Override local userdata
//        val newData = data.copy(languages = languages, learningLanguageId = currentLanguage.id)
//        accountModule.setUserData(newData)

        // We don't care about actual return result
        return service.switchLanguage(new SwitchLanguageController(language.getId())).toObservable()
                .map(data -> new Object());
    }

}
