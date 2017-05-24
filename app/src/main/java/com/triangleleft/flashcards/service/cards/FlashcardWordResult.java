package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FlashcardWordResult {

    public abstract FlashcardWord getWord();

    public abstract boolean isCorrect();

    public static FlashcardWordResult create(FlashcardWord word, boolean correct) {
        return new AutoValue_FlashcardWordResult(word, correct);
    }

}
