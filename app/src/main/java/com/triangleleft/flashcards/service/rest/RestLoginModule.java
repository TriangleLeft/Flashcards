package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.IloginListener;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.LoginError;
import com.triangleleft.flashcards.service.error.LoginFieldError;
import com.triangleleft.flashcards.service.error.NetworkError;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestLoginModule implements ILoginModule {
    private final IDuolingoRest service;
    private boolean logged;
    private IloginListener listener;

    @Inject
    public RestLoginModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public void login(String login, String password) {
        Call<LoginResponseModel> loginCall = service.login(login, password);
        loginCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Response<LoginResponseModel> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    LoginResponseModel model = response.body();
                    if (model.isSuccess()) {
                        listener.onSuccess();
                    } else {
                        notifyError(new LoginFieldError(model.message, model.getErrorField()));
                    }
                } else {
                    // TODO: pass exact cause if any
                    notifyError(new LoginError(null));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: pass exact cause
                notifyError(new NetworkError(null));
            }
        });
    }

    private void notifySuccess() {
        if (listener != null) {
            listener.onSuccess();
        }
    }

    private void notifyError(CommonError error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    @Override
    public void registerListener(IloginListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener(IloginListener listener) {
        if (this.listener == listener) {
            this.listener = null;
        }
    }
}
