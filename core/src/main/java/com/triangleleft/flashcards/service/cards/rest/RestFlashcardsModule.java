package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.common.AbstractProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class RestFlashcardsModule extends AbstractProvider implements IFlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(RestFlashcardsModule.class);

    private final IDuolingoRest service;

    /**
     * Create new RestFlashcardsModule.
     */
    public RestFlashcardsModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public Observable<List<IFlashcardWord>> getFlashcards() {
        return service.getFlashcardData(15, true, System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .map(model -> model.getWords());
    }

}
