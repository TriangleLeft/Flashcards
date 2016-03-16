package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.error.ErrorType;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.provider.AbstractProvider;
import com.triangleleft.flashcards.service.provider.SimpleProviderResult;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.util.IPersistentStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

public class RestLoginModule extends AbstractProvider<ILoginRequest, ILoginResult> implements ILoginModule {


    private static final Logger log = LoggerFactory.getLogger(RestLoginModule.class);
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
    protected void processRequest(@NonNull ILoginRequest request) {
        log.debug("processRequest() called with: {}", request);
        Call<LoginResponseModel> loginCall = service.login(request.getLogin(), request.getPassword());
        loginCall.enqueue(new LoginResponseCallback(request));
    }

    @Override
    public void cancelRequest(@NonNull ILoginRequest loginRequest) {

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

    @Override
    protected void notifyResult(@NonNull ILoginRequest request, @Nullable ILoginResult result,
                                @Nullable CommonError error) {
        if (result != null) {
            setLoginStatus(result.getResult());
        } else if (error != null) {
            setLoginStatus(LoginStatus.NOT_LOGGED);
        }
        // Case when both result and error are null is handled by parent
        super.notifyResult(request, result, error);
    }

    private class LoginResponseCallback extends AbstractCallback<LoginResponseModel> {

        public LoginResponseCallback(@NonNull ILoginRequest request) {
            super(request);
        }

        @Override
        public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
            log.debug("onResponse() called with: call = [{}], response = [{}]", call, response);
            ILoginResult result = null;
            CommonError error = null;
            if (response.isSuccess()) {
                LoginResponseModel model = response.body();
                if (model.isSuccess()) {
                    result = (ILoginResult) new SimpleProviderResult<>(LoginStatus.LOGGED);
                } else {
                    error = model.buildError();
                }
            } else {
                // Non-200 response code, for now, we don't expect them
                error = new CommonError(ErrorType.SERVER, response.message());
            }

            notifyResult(getRequest(), result, error);
        }
    }

}