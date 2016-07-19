package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class RestFlashcardsModule implements IFlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(RestFlashcardsModule.class);
    private static final int FLASHCARDS_COUNT = 15;

    private final RestService service;

    /**
     * Create new RestFlashcardsModule.
     */
    @Inject
    public RestFlashcardsModule(RestService service) {
        this.service = service;
    }

    @Override
    public Observable<FlashcardTestData> getFlashcards() {
        logger.debug("getFlashcards() called");
        return service.getFlashcardData(FLASHCARDS_COUNT, true, System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .cast(FlashcardTestData.class);
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
