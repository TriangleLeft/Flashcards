package com.triangleleft.flashcards.service.vocabular.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import rx.Observable;

@FunctionsAreNonnullByDefault
public class RestVocabularModule extends AbstractProvider implements IVocabularModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularModule.class);
    private final IDuolingoRest service;
    private VocabularData cachedData;

    @Inject
    public RestVocabularModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public Observable<VocabularData> getVocabularData(boolean refresh) {
        logger.debug("getVocabularList() called");
        Observable<VocabularData> observable = service.getVocabularList(System.currentTimeMillis())
                .map(VocabularResponseModel::toVocabularData)
                .doOnNext(this::updateCache);
        // For fresh calls, try to return db cache
        if (!refresh && cachedData != null) {
            observable = observable.startWith(getCachedData());
        }
        return observable;
    }

    private void updateCache(VocabularData data) {
        cachedData = data;
    }

    @Nullable
    private VocabularData getCachedData() {
        return cachedData;
    }

}
