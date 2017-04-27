package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import io.reactivex.Observable;


@FunctionsAreNonnullByDefault
public interface FlashcardsModule {

    Observable<FlashcardTestData> getFlashcards();

    Observable<FlashcardTestData> getLocalFlashcards();

    void postResult(FlashcardTestResult results);
}
