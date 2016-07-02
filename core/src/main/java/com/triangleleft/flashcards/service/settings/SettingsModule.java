package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.Nullable;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    @Nullable
    UserData getCurrentUserData();

    Observable<UserData> getUserData();

    Observable<Void> switchLanguage(Language language);
}
