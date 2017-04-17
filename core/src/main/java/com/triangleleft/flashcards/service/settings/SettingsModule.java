package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    Observable<UserData> loadUserData();

    Call<Object> switchLanguage(Language language);
}
