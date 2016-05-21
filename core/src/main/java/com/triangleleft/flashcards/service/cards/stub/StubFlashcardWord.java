package com.triangleleft.flashcards.service.cards.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.cards.IFlashcardWord;

@AutoValue
public abstract class StubFlashcardWord implements IFlashcardWord {

    public static IFlashcardWord create(String word, String translation, String id) {
        return new AutoValue_StubFlashcardWord(word, translation, id);
    }
}
