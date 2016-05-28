package com.triangleleft.flashcards.service.vocabular.stub;

import com.triangleleft.flashcards.service.common.IProviderRequest;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class StubVocabularModule implements IVocabularModule {

    private List<IVocabularWord> list = Arrays.asList(
            StubVocabularWord.create("word1", 1),
            StubVocabularWord.create("word2", 2),
            StubVocabularWord.create("word3", 3));


    @Override
    public Observable<List<IVocabularWord>> getVocabularList(boolean refresh) {
        return Observable.just(list);
    }

    @Override
    public void cancelRequest(IProviderRequest loginRequest) {

    }
}
