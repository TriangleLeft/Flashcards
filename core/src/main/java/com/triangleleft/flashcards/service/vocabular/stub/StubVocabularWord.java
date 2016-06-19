package com.triangleleft.flashcards.service.vocabular.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.vocabular.VocabularWord;

@AutoValue
public abstract class StubVocabularWord implements VocabularWord {

    public static VocabularWord create(String word, int strength) {
        return new AutoValue_StubVocabularWord(word, strength);
    }
}
