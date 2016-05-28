package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class FlashcardTestResult {

    public abstract String getUiLanguage();

    public abstract String getLearningLanguage();

    public abstract List<FlashcardWordResult> getWordResults();

    public static FlashcardTestResult create(String uiLanguage, String learningLanguage,
                                             List<FlashcardWordResult> wordResults) {
        return new AutoValue_FlashcardTestResult(uiLanguage, learningLanguage, wordResults);
    }
}
