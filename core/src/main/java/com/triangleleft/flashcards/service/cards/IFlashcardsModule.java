package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface IFlashcardsModule {

    Observable<FlashcardTestData> getFlashcards();

    void postResult(FlashcardTestResult results);
}
