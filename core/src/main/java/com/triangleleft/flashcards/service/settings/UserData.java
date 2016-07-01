package com.triangleleft.flashcards.service.settings;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.AutoGson;

import java.util.List;

@AutoGson
@AutoValue
public abstract class UserData {

    public abstract List<Language> getLanguages();

    public abstract String getAvatar();

    public abstract String getUsername();

    public abstract String getUiLanguageId();

    public abstract String getLearningLanguageId();

    public static UserData create(List<Language> languages, String avatar, String username, String learningLanguage,
                                  String uiLanguage) {
        return new AutoValue_UserData(languages, avatar, username, learningLanguage, uiLanguage);
    }

}
