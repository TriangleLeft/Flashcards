package com.triangleleft.flashcards.service.cards.stub;

import com.google.auto.value.AutoValue;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

@AutoValue
public abstract class StubFlashcardTestData implements FlashcardTestData {

    public static FlashcardTestData create(String uiLanguage, String learningLanguage, List<FlashcardWord> wordList) {
        return new AutoValue_StubFlashcardTestData(uiLanguage, learningLanguage, wordList);
    }
}
