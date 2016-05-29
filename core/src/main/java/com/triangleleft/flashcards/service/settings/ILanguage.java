package com.triangleleft.flashcards.service.settings;

public interface ILanguage {

    String getId();

    String getName();

    int getLevel();

    boolean isLearning();

    boolean isCurrentLearning();
}
