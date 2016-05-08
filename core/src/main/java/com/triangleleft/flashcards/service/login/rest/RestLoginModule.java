package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.login.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.util.IPersistentStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import retrofit2.Call;

public class RestLoginModule extends AbstractProvider implements ILoginModule {


    private static final Logger logger = LoggerFactory.getLogger(RestLoginModule.class);

    private static final String LOGIN_KEY = "RestLoginModule:loginKey";

    private final IDuolingoRest service;
    private final IPersistentStorage storage;
    private LoginStatus loginStatus;

    public RestLoginModule(@NonNull IDuolingoRest service, @NonNull IPersistentStorage storage) {
        this.service = service;
        this.storage = storage;
        loginStatus = storage.get(LOGIN_KEY, LoginStatus.class, LoginStatus.NOT_LOGGED);
    }

    @Override
    public void login(ILoginRequest request, IListener<ILoginResult> listener) {
        logger.debug("processRequest() called with: {}", request);
        Call<LoginResponseModel> loginCall = service.login(request.getLogin(), request.getPassword());
        loginCall.enqueue(new LoginResponseCallback(request, listener));
    }

    @NonNull
    @Override
    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    private void setLoginStatus(@NonNull LoginStatus status) {
        loginStatus = status;
        storage.put(LOGIN_KEY, status);
    }

    private class LoginResponseCallback extends AbstractCallback<LoginResponseModel> {


        private final IListener<ILoginResult> listener;

        public LoginResponseCallback(ILoginRequest request, IListener<ILoginResult> listener) {
            super(request);
            this.listener = listener;
        }

        @Override
        protected void onError(CommonError error) {
            setLoginStatus(LoginStatus.NOT_LOGGED);
            listener.onFailure(error);
        }

        @Override
        protected void onResult(LoginResponseModel result) {
            if (result.isSuccess()) {
                setLoginStatus(LoginStatus.LOGGED);
                listener.onResult(() -> LoginStatus.LOGGED);
            } else {
                onError(result.buildError());
            }
        }
    }

}