package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class SimpleVocabularyData implements VocabularyData {

    public static VocabularyData create(List<VocabularyWord> words, String uiLanguageId, String learningLanguageId) {
        return new AutoValue_SimpleVocabularyData(words, uiLanguageId, learningLanguageId);
    }
}
