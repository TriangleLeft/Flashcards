package com.triangleleft.flashcards.service.adapter;

import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.service.common.exception.NetworkException;
import com.triangleleft.flashcards.service.common.exception.ServerException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FlashcardsCallAdapterFactory extends CallAdapter.Factory {


    public FlashcardsCallAdapterFactory() {
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != com.triangleleft.flashcards.Call.class) {
            return null;
        }

        Type type = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new SimpleCallAdapter(type);
    }

    static final class SimpleCallAdapter implements CallAdapter<com.triangleleft.flashcards.Call<?>> {
        private final Type responseType;

        SimpleCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> com.triangleleft.flashcards.Call<R> adapt(Call<R> call) {
            return new com.triangleleft.flashcards.Call<R>() {
                @Override
                public void enqueue(Action<R> onData, Action<Throwable> onError) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            if (response.isSuccessful()) {
                                onData.call(response.body());
                            } else {
                                try {
                                    onError.call(
                                            new ServerException("Non-200 code for " + response.errorBody().string()));
                                } catch (IOException e) {
                                    onError.call(new ServerException());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable t) {
                            if (t instanceof IOException) {
                                onError.call(new NetworkException(t));
                            } else {
                                onError.call(t);
                            }
                        }
                    });
                }

                @Override
                public void cancel() {
                    call.cancel();
                }
            };
        }
    }
}
