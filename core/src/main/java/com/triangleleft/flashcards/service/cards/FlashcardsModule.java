package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;


@FunctionsAreNonnullByDefault
public interface FlashcardsModule {

    void getFlashcards(Observer<FlashcardTestData> observer);

    void postResult(FlashcardTestResult results);
}
