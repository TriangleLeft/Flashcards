package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.provider.AbstractProvider;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.SimpleLoginResult;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestLoginModule extends AbstractProvider<ILoginRequest, ILoginResult> implements ILoginModule {


    private static final Logger log = LoggerFactory.getLogger(RestLoginModule.class);

    private final IDuolingoRest service;

    @Inject
    public RestLoginModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    protected void processRequest(@NonNull ILoginRequest request) {
        log.debug("processRequest() called with: {}", request);
        Call<LoginResponseModel> loginCall = service.login(request.getLogin(), request.getPassword());
        loginCall.enqueue(new LoginResponseCallback(request));
    }

    @Override
    public void cancelRequest(@NonNull ILoginRequest loginRequest) {

    }

    private class LoginResponseCallback implements Callback<LoginResponseModel> {

        private final ILoginRequest request;

        public LoginResponseCallback(@NonNull ILoginRequest request) {
            this.request = request;
        }

        @Override
        public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
            log.debug("onResponse() called with: call = [{}], response = [{}]", call, response);
            SimpleLoginResult result = null;
            CommonError error = null;
            if (response.isSuccess()) {
                LoginResponseModel model = response.body();
                if (model.isSuccess()) {
                    result = new SimpleLoginResult(LoginStatus.LOGGED);
                } else {
                    error = model.buildError();
                }
            } else {
                // Non-200 response code, for now, we don't expect them
                error = new CommonError(ErrorType.SERVER, response.message());
            }

            notifyResult(request, result, error);
        }

        @Override
        public void onFailure(Call<LoginResponseModel> call, Throwable t) {
            log.debug("onFailure() called with: call = [{}], t = [{}]", call, t);
            notifyResult(request, null, CommonError.fromThrowable(t));
        }
    }

}