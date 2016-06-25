package com.triangleleft.flashcards.service.vocabular;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
@AutoValue
public abstract class SimpleVocabularWord implements VocabularWord {

    public static VocabularWord create(String word, int strength) {
        return new AutoValue_SimpleVocabularWord(word, strength);
    }
}
