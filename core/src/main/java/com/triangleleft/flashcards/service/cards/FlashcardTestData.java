package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@AutoValue
@FunctionsAreNonnullByDefault
public abstract class FlashcardTestData {

    public static FlashcardTestData create(String uiLanguage, String learningLanguage, List<FlashcardWord> wordList) {
        return new AutoValue_FlashcardTestData(uiLanguage, learningLanguage, wordList);
    }

    public abstract String getUiLanguage();

    public abstract String getLearningLanguage();

    public abstract List<FlashcardWord> getWords();
}
