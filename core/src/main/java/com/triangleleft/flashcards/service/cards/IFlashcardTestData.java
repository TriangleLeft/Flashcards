package com.triangleleft.flashcards.service.cards;

import java.util.List;

public interface IFlashcardTestData {

    String getUiLanguage();

    String getLearningLanguage();

    List<IFlashcardWord> getWords();
}
