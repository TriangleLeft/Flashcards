package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface ISettingsModule {

    Observable<IUserData> getUserData();

    Observable<Void> switchLanguage(ILanguage language);
}