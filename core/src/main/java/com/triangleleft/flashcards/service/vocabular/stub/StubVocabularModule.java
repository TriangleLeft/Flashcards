package com.triangleleft.flashcards.service.vocabular.stub;

import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularData;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularWord;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class StubVocabularModule implements IVocabularModule {

    private final VocabularData data;

    @Inject
    public StubVocabularModule() {
        List<VocabularWord> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(SimpleVocabularWord.create("word" + i, (int) (Math.random() * 4)));
        }
        data = SimpleVocabularData.create(list, "en", "es");
    }

    @Override
    public Observable<VocabularData> getVocabularData(boolean refresh) {
        return Observable.just(data);
    }

}
