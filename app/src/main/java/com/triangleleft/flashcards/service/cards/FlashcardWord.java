package com.triangleleft.flashcards.service.cards;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@AutoValue
@FunctionsAreNonnullByDefault
public abstract class FlashcardWord {

    public static FlashcardWord create(String word, String translation, String id) {
        return new AutoValue_FlashcardWord(word, translation, id);
    }

    public abstract String getWord();

    public abstract String getTranslation();

    public abstract String getId();
}
