package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.cards.FlashcardTestData;
import com.triangleleft.flashcards.service.cards.FlashcardTestResult;
import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.service.common.exception.ServerException;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void getFlashcards(Observer<FlashcardTestData> observer) {
        logger.debug("getFlashcards() called");
        // TODO: consider moving flashcard count constant to settings
        service.getFlashcardData(FLASHCARDS_COUNT, true, System.currentTimeMillis())
                .enqueue(new Callback<FlashcardResponseModel>() {
                    @Override
                    public void onResponse(Call<FlashcardResponseModel> call,
                                           Response<FlashcardResponseModel> response) {
                        if (response.isSuccessful()) {
                            FlashcardTestData data = response.body().toTestData();
                            observer.onNext(data);
                        } else {
                            observer.onError(new ServerException("Got non-200 code from getFlashcardData()"));
                        }
                    }

                    @Override
                    public void onFailure(Call<FlashcardResponseModel> call, Throwable t) {
                        observer.onError(t);
                    }
                });
    }

    @Override
    public void postResult(FlashcardTestResult result) {
        logger.debug("postResult() called with: result = [{}]", result);
        // NOTE: We don't care about whether we were able to send results
        service.postFlashcardResults(new FlashcardResultsController(result))
                .subscribe(whatever -> {
                }, throwable -> logger.error("Failed to post results", throwable));
    }

}
