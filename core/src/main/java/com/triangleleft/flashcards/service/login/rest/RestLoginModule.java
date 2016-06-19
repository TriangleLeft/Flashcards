package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.common.AbstractProvider;
import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.service.login.rest.model.LoginResponseModel;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.PersistentStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Call;

@FunctionsAreNonnullByDefault
public class RestLoginModule extends AbstractProvider implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginModule.class);

    private static final String KEY_USER_ID = "RestLoginModule::userId";
    private static final String KEY_LOGIN = "RestLoginModule::login";

    private final IDuolingoRest service;
    private final PersistentStorage storage;
    private String userId;
    private String login;

    @Inject
    public RestLoginModule(IDuolingoRest service, PersistentStorage storage) {
        this.service = service;
        this.storage = storage;
        userId = storage.get(KEY_USER_ID, String.class);
        login = storage.get(KEY_LOGIN, String.class);
    }

    @Override
    public void login(ILoginRequest request, IListener<ILoginResult> listener) {
        logger.debug("processRequest() called with: {}", request);
        setLogin(request.getLogin());
        Call<LoginResponseModel> loginCall = service.login(request.getLogin(), request.getPassword());
        loginCall.enqueue(new LoginResponseCallback(request, listener));
    }

    @NonNull
    @Override
    public LoginStatus getLoginStatus() {
        return userId == null ? LoginStatus.NOT_LOGGED : LoginStatus.LOGGED;
    }

    @Nullable
    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getLogin() {
        return login;
    }

    private void setUserId(@Nullable String userId) {
        this.userId = userId;
        storage.put(KEY_USER_ID, userId);
    }

    private void setLogin(@Nullable String login) {
        this.login = login;
        storage.put(KEY_LOGIN, login);
    }

    private class LoginResponseCallback extends AbstractCallback<LoginResponseModel> {


        private final IListener<ILoginResult> listener;

        public LoginResponseCallback(ILoginRequest request, IListener<ILoginResult> listener) {
            super(request);
            this.listener = listener;
        }

        @Override
        protected void onError(CommonError error) {
            // Clear previously saved userId
            setUserId(null);
            listener.onFailure(error);
        }

        @Override
        protected void onResult(LoginResponseModel result) {
            if (result.isSuccess()) {
                setUserId(result.userId);
                listener.onResult(() -> LoginStatus.LOGGED);
            } else {
                onError(result.buildError());
            }
        }
    }

}