package com.triangleleft.flashcards.service.settings;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.AutoGson;

import java.util.List;

@AutoGson
@AutoValue
public abstract class UserData {

    public static UserData create(
        List<Language> languages,
        Optional<String> avatar,
        Optional<String> username,
        String uiLanguage,
        String learningLanguage) {
        return new AutoValue_UserData(
            languages,
            avatar,
            username,
            uiLanguage,
            learningLanguage);
    }

    public abstract List<Language> getLanguages();

    public abstract Optional<String> getAvatar();

    public abstract Optional<String> getUsername();

    public abstract String getUiLanguageId();

    public abstract String getLearningLanguageId();

}
