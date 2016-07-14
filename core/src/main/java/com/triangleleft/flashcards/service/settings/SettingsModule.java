package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    Observable<UserData> loadUserData();

    Observable<Void> switchLanguage(Language language);
}
