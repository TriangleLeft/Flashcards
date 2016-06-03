package com.triangleleft.flashcards.service.vocabular.stub;

import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubVocabularModule implements IVocabularModule {

    @Inject
    public StubVocabularModule() {

    }

    private List<IVocabularWord> list = Arrays.asList(
            StubVocabularWord.create("word1", 1),
            StubVocabularWord.create("word2", 2),
            StubVocabularWord.create("word3", 3));


    @Override
    public Observable<List<IVocabularWord>> getVocabularList(boolean refresh) {
        return Observable.just(list);
    }

}
