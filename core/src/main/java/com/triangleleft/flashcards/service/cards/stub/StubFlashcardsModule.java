package com.triangleleft.flashcards.service.cards.stub;

import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.common.IProviderRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class StubFlashcardsModule implements IFlashcardsModule {

    private final List<IFlashcardWord> words = new ArrayList<>();

    public StubFlashcardsModule() {
        for (int i = 0; i < 5; i++) {
            words.add(StubFlashcardWord.create("word"+i,"translation"+i,"id"+i));
        }
    }

    @Override
    public Observable<List<IFlashcardWord>> getFlashcards() {
        return Observable.just(words);
    }

    @Override
    public void cancelRequest(IProviderRequest loginRequest) {

    }
}
