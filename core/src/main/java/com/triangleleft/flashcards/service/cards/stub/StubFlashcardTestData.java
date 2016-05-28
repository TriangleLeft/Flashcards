package com.triangleleft.flashcards.service.cards.stub;

import com.google.auto.value.AutoValue;

import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import java.util.List;

@AutoValue
public abstract class StubFlashcardTestData implements IFlashcardTestData {

    public static IFlashcardTestData create(String uiLanguage, String learningLanguage, List<IFlashcardWord> wordList) {
        return new AutoValue_StubFlashcardTestData(uiLanguage, learningLanguage, wordList);
    }
}
