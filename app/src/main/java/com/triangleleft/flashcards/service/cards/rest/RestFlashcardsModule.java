package com.triangleleft.flashcards.service.cards.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.IFlashcardsRequest;
import com.triangleleft.flashcards.service.cards.IFlashcardsResult;
import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.IProviderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;

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
    public void getFlashcards(IFlashcardsRequest request, IListener<IFlashcardsResult> listener) {
        logger.debug("getFlashcards() called with: request = [{}], listener = [{}]", request, listener);
        Call<FlashcardResponseModel> call = service.getFlashcardData();
        call.enqueue(new FlashcardsCallback(request, listener));
        addRequest(request, call);
    }

    private class FlashcardsCallback extends AbstractCallback<FlashcardResponseModel> {

        private final IListener<IFlashcardsResult> listener;

        public FlashcardsCallback(IProviderRequest request, IListener<IFlashcardsResult> listener) {
            super(request);
            this.listener = listener;
        }


        @Override
        protected void onError(CommonError error) {
            listener.onFailure(error);
        }

        @Override
        protected void onResult(FlashcardResponseModel result) {
            listener.onResult(() -> result.getWords());
        }
    }
}
