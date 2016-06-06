package com.triangleleft.flashcards.service.vocabular.stub;

import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubVocabularModule implements IVocabularModule {

    private final List<IVocabularWord> list;

    @Inject
    public StubVocabularModule() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(StubVocabularWord.create("word" + i, (int) (Math.random() * 4)));
        }
    }

    @Override
    public Observable<List<IVocabularWord>> getVocabularList(boolean refresh) {
        return Observable.just(list);
    }

}
