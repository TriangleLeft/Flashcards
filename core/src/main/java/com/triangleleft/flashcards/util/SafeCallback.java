package com.triangleleft.flashcards.util;

import com.triangleleft.flashcards.service.common.exception.ServerException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SafeCallback<T> implements Callback<T> {

    public abstract void onResult(T result);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            try {
                onResult(response.body());
            } catch (Exception e) {
                onFailure(call, e);
            }
        } else {
            onFailure(call, new ServerException("Non-200 result"));
        }
    }
}
