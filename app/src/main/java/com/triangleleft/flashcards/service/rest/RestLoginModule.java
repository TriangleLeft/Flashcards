package com.triangleleft.flashcards.service.rest;

import com.triangleleft.assertdialog.AssertDialog;
import com.triangleleft.flashcards.service.ICommonListener;
import com.triangleleft.flashcards.service.ILoginModule;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.service.rest.model.LoginResponseModel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestLoginModule implements ILoginModule {
    private static final String TAG = RestLoginModule.class.getSimpleName();
    private final IDuolingoRest service;
    private final Set<ICommonListener> listeners =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Inject
    public RestLoginModule(IDuolingoRest service) {
        this.service = service;
    }

    @Override
    public boolean isLogged() {
        return false;
    }

    @Override
    public void login(@NonNull String login, @Nullable String password) {
        Log.d(TAG, "login() called with: " + "login = [" + login + "], password = [" + password + "]");
        AssertDialog.assertTrue(listeners.size() > 0, "Calling login() without any listeners!");
        Call<LoginResponseModel> loginCall = service.login(login, password);
        loginCall.enqueue(new LoginResponseCallback());
    }

    @Override
    public void registerListener(@NonNull ICommonListener listener) {
        Log.d(TAG, "registerListener() called with: " + "listener = [" + listener + "]");
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(@NonNull ICommonListener listener) {
        Log.d(TAG, "unregisterListener() called with: " + "listener = [" + listener + "]");
        listeners.remove(listener);
    }

    private void notifySuccess() {
        Log.d(TAG, "notifySuccess() called");
        for (ICommonListener listener : listeners) {
            listener.onSuccess();
        }
    }

    private void notifyFailure(@NonNull CommonError error) {
        Log.d(TAG, "notifyFailure() called with: " + "error = [" + error + "]");
        for (ICommonListener listener : listeners) {
            listener.onError(error);
        }
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