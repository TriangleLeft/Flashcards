package com.triangleleft.flashcards.service.cards.stub;

import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.common.IProviderRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class StubFlashcardsModule implements IFlashcardsModule {

    private final IFlashcardTestData testData;

    public StubFlashcardsModule() {
        List<IFlashcardWord> words = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            words.add(StubFlashcardWord.create("word" + i, "translation" + i, "id" + i));
        }
        testData = null;
    }

    @Override
    public Observable<IFlashcardTestData> getFlashcards() {
        return Observable.just(testData);
    }

    @Override
    public void postResult(FlashcardTestResult results) {

    }

    @Override
    public void cancelRequest(IProviderRequest loginRequest) {

    }
}
