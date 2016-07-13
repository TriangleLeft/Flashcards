package com.triangleleft.flashcards.service.cards;

import java.util.List;

public interface FlashcardTestData {

    String getUiLanguage();

    String getLearningLanguage();

    List<FlashcardWord> getWords();
}
