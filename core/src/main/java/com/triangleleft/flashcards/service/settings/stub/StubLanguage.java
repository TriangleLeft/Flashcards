package com.triangleleft.flashcards.service.settings.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.settings.ILanguage;

@AutoValue
public abstract class StubLanguage implements ILanguage {

    public static StubLanguage create(String id, String name, int level, boolean learning, boolean currentLearning) {
        return new AutoValue_StubLanguage(id, name, level, learning, currentLearning);
    }

    public StubLanguage withCurrentLearning(boolean currentLearning) {
        return new AutoValue_StubLanguage(getId(), getName(), getLevel(), isLearning(), currentLearning);
    }
}
