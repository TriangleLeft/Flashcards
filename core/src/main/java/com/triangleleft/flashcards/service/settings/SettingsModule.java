package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    Call<UserData> loadUserData();

    Call<Object> switchLanguage(Language language);
}
