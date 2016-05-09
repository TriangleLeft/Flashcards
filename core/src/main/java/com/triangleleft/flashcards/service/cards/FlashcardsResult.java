package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FlashcardsResult {

    public abstract String getId();

    public abstract boolean isCorrect();

}
