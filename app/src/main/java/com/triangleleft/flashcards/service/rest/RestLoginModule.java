package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.AbstractProvider;
import com.triangleleft.flashcards.service.SimpleLoginRequest;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestLoginModule extends AbstractProvider<SimpleLoginRequest, RestLoginResult> {
    private static final String TAG = RestLoginModule.class.getSimpleName();
    private final IDuolingoRest service;
    private Set<SimpleLoginRequest> requests = Collections.newSetFromMap(new ConcurrentHashMap<>());


    @Inject
    public RestLoginModule(IDuolingoRest service) {
        this.service = service;
    }

    @NonNull
    @Override
    public RestLoginResult getResult(@NonNull SimpleLoginRequest request) {
        return null;
    }

    @Override
    public void doRequest(@NonNull SimpleLoginRequest request) {
        Log.d(TAG, "doRequest() called with: " + "request = [" + request + "]");
        requests.add(request);
        Call<LoginResponseModel> loginCall = service.login(request.getLogin(), request.getPassword());
        loginCall.enqueue(new LoginResponseCallback());
    }

    private class LoginResponseCallback implements Callback<LoginResponseModel> {

        @Override
        public void onResponse(Response<LoginResponseModel> response, Retrofit retrofit) {
            Log.d(TAG, "onResponse() called with: " + "response = [" + response + "], retrofit = [" + retrofit + "]");
            if (response.isSuccess()) {
                LoginResponseModel model = response.body();
                if (model.isSuccess()) {
                    notifySuccess();
                } else {
                    notifyFailure(new CommonError("model failure"));
                }
            } else {
                notifyFailure(new CommonError("responce failure"));
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Log.d(TAG, "onFailure() called with: " + "t = [" + t + "]");
            notifyFailure(new CommonError("internal"));
        }
    }

}