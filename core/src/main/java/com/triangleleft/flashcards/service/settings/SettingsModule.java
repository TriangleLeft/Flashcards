package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    Observable<UserData> userData();

    Observable<Object> switchLanguage(Language language);
}
