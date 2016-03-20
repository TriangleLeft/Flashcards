package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.provider.AbstractProvider;
import com.triangleleft.flashcards.service.rest.model.VocabularResponseModel;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularRequest;
import com.triangleleft.flashcards.service.vocabular.IVocabularResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Response;

public class RestVocabularModule extends AbstractProvider<IVocabularRequest, IVocabularResult>
        implements IVocabularModule {

    private static final Logger logger = LoggerFactory.getLogger(RestVocabularModule.class);

    private final IDuolingoRest service;

    public RestVocabularModule(@NonNull IDuolingoRest service) {
        this.service = service;
    }


    @Override
    protected void processRequest(@NonNull IVocabularRequest request) {
        logger.debug("processRequest() called with: request = [{}]", request);
        Call<VocabularResponseModel> loginCall = service.getVocabularList(System.currentTimeMillis());
        loginCall.enqueue(new VocabularResponseCallback(request));
    }

    @Override
    public void cancelRequest(@NonNull IVocabularRequest loginRequest) {

    }

    private class VocabularResponseCallback extends AbstractCallback<VocabularResponseModel> {

        public VocabularResponseCallback(@NonNull IVocabularRequest request) {
            super(request);
        }

        @Override
        public void onResponse(Call<VocabularResponseModel> call, Response<VocabularResponseModel> response) {
            logger.debug("onResponse() called with: call = [{}], response = [{}]", call, response);
            IVocabularResult result = null;
            CommonError error = null;
            if (response.isSuccess()) {
                VocabularResponseModel model = response.body();
                result = new IVocabularResultImpl(model.getWords());
            } else {
                // Non-200 response code, for now, we don't expect them
                error = new CommonError(ErrorType.SERVER, response.message());
            }

            notifyResult(getRequest(), result, error);
        }
    }
}
