package com.triangleleft.flashcards.service.settings;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface SettingsModule {

    void loadUserData(Observer<UserData> observer);

    void switchLanguage(Language language, Observer<Void> observer);
}
