package com.triangleleft.flashcards.service.settings;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class SimpleUserData implements UserData {

    public static UserData create(List<ILanguage> languages, String avatar, String username, String learningLanguage,
                                  String uiLanguage) {
        return new AutoValue_SimpleUserData(languages, avatar, username, learningLanguage, uiLanguage);
    }

}
