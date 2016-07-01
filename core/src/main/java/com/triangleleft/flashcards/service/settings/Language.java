package com.triangleleft.flashcards.service.settings;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.AutoGson;

@AutoGson
@AutoValue
public abstract class Language {

    public abstract String getId();

    public abstract String getName();

    public abstract int getLevel();

    public abstract boolean isLearning();

    public abstract boolean isCurrentLearning();

    public static Language create(String id, String name, int level, boolean learning, boolean currentLearning) {
        return new AutoValue_Language(id, name, level, learning, currentLearning);
    }

    public abstract Language withCurrentLearning(boolean currentLearning);
}
