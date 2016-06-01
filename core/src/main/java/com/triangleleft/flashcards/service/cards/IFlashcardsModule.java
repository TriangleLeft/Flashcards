package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.service.common.IProvider;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface IFlashcardsModule extends IProvider {

    Observable<IFlashcardTestData> getFlashcards();

    void postResult(FlashcardTestResult results);
}
