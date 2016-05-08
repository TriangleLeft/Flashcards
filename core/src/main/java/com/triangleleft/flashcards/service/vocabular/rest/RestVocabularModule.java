package com.triangleleft.flashcards.service.vocabular.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class RestVocabularModule extends AbstractProvider implements IVocabularModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularModule.class);

    // FIXME: use proper cache
    private volatile List<IVocabularWord> cachedList = Collections.singletonList(new IVocabularWord() {
        @Override
        public String getWord() {
            return "Cached word!";
        }

        @Override
        public int getStrength() {
            return 0;
        }
    });

    private final IDuolingoRest service;

    public RestVocabularModule(@NonNull IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public Observable<List<IVocabularWord>> getVocabularList(boolean refresh) {
        logger.debug("getVocabularList() called");
        Observable<List<IVocabularWord>> observable = service.getVocabularList(System.currentTimeMillis())
                .map(model -> model.getWords())
                .subscribeOn(Schedulers.io())
                .doOnNext(list -> setCachedList(list));
        // For fresh calls, try to return db cache
        if (!refresh) {
            observable = observable.startWith(getCachedList());
        }
        return observable;
    }

    private void setCachedList(List<IVocabularWord> list) {
        logger.debug("setCachedList() called with: list = [{}]", list);
        cachedList = list;
    }

    private List<IVocabularWord> getCachedList() {
        logger.debug("getCachedList() called");
        return cachedList;
    }

}
