package com.triangleleft.flashcards.service.vocabular.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

@AutoValue
public abstract class StubVocabularWord implements IVocabularWord {

    public static IVocabularWord create(String word, int strength) {
        return new AutoValue_StubVocabularWord(word, strength);
    }
}
