package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.service.common.IProvider;

import rx.Observable;

public interface IFlashcardsModule extends IProvider {

    Observable<IFlashcardTestData> getFlashcards();

    void postResult(FlashcardTestResult results);
}
