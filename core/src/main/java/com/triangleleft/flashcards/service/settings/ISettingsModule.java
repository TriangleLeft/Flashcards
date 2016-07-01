package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface ISettingsModule {

    UserData getCurrentUserData();

    Observable<UserData> getUserData();

    Observable<Void> switchLanguage(Language language);
}
