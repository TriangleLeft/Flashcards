package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FlashcardWordResult {

    public abstract String getId();

    public abstract boolean isCorrect();

    public static FlashcardWordResult create(String id, boolean correct) {
        return new AutoValue_FlashcardWordResult(id, correct);
    }

}
