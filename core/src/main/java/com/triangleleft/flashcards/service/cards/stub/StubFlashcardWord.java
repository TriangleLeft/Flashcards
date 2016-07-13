package com.triangleleft.flashcards.service.cards.stub;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

@AutoValue
public abstract class StubFlashcardWord implements FlashcardWord {

    public static FlashcardWord create(String word, String translation, String id) {
        return new AutoValue_StubFlashcardWord(word, translation, id);
    }
}
