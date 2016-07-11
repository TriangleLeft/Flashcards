package com.triangleleft.flashcards.service.settings;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    Optional<UserData> getCurrentUserData();

    Observable<UserData> getUserData();

    Observable<Void> switchLanguage(Language language);
}
