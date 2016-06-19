package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class SimpleVocabularData implements VocabularData {

    public static VocabularData create(List<VocabularWord> words, String uiLanguageId, String learningLanguageId) {
        return new AutoValue_SimpleVocabularData(words, uiLanguageId, learningLanguageId);
    }
}
