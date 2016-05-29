package com.triangleleft.flashcards.service.settings;

import rx.Observable;

public interface ISettingsModule {

    Observable<IUserData> getUserData();

    Observable<Void> switchLanguage(ILanguage language);
}
