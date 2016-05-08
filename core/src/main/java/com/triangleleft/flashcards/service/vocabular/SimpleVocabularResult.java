package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class SimpleVocabularResult implements IVocabularResult {
    public static SimpleVocabularResult create(List<IVocabularWord> result, boolean fromCache) {
        return new AutoValue_SimpleVocabularResult(result, fromCache);
    }
}
