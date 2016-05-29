package com.triangleleft.flashcards.service.settings.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.IUserData;

import java.util.List;

@AutoValue
public abstract class StubUserData implements IUserData {

    public static IUserData create(List<ILanguage> languages, String avatar, String username) {
        return new AutoValue_StubUserData(languages, avatar, username);
    }
}
