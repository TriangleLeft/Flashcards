package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class RestFlashcardsModule implements FlashcardsModule {

    private static final Logger logger = LoggerFactory.getLogger(RestFlashcardsModule.class);
    private static final int FLASHCARDS_COUNT = 15;

    private final RestService service;

    @Inject
    public RestFlashcardsModule(RestService service) {
        this.service = service;
    }

    @Override
    public Observable<FlashcardTestData> getFlashcards() {
        logger.debug("getFlashcards() called");
        // TODO: consider moving flashcard count this to settings
        return service.getFlashcardData(FLASHCARDS_COUNT, true, System.currentTimeMillis())
            .map(FlashcardResponseModel::toTestData);
    }

    @Override
    public void postResult(FlashcardTestResult result) {
        logger.debug("postResult() called with: result = [{}]", result);
        // NOTE: We don't care about whether we were able to send results
        service.postFlashcardResults(new FlashcardResultsController(result))
                .subscribe(
                        response -> logger.debug("response = [{}]", response),
                        error -> logger.debug("error = [{}]", error)
                );
    }

}
