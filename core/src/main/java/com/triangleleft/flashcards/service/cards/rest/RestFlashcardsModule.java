package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.IFlashcardTestData;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

@FunctionsAreNonnullByDefault
public class RestFlashcardsModule extends AbstractProvider implements IFlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(RestFlashcardsModule.class);

    private final IDuolingoRest service;

    /**
     * Create new RestFlashcardsModule.
     */
    @Inject
    public RestFlashcardsModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public Observable<IFlashcardTestData> getFlashcards() {
        logger.debug("getFlashcards() called");
        return service.getFlashcardData(15, true, System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .cast(IFlashcardTestData.class);
    }

    @Override
    public void postResult(FlashcardTestResult result) {
        logger.debug("postResult() called with: result = [{}]", result);
        service.postFlashcardResults(new FlashcardResultsController(result))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        response -> logger.debug("response = [{}]", response),
                        error -> logger.debug("error = [{}]", error)
                );
    }

}
