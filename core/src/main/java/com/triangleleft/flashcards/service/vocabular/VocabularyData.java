package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class VocabularyData {

    public abstract List<VocabularyWord> getWords();

    public abstract String getLearningLanguageId();

    public abstract String getUiLanguageId();

    public static VocabularyData create(List<VocabularyWord> words, String uiLanguageId, String learningLanguageId) {
        return new AutoValue_VocabularyData(words, learningLanguageId, uiLanguageId);
    }
}
