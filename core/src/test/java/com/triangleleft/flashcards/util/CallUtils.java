package com.triangleleft.flashcards.util;

import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.Call;

public class CallUtils {

    public static <T> Call<T> just(T data) {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {
                onData.call(data);
            }

            @Override
            public void cancel() {

            }
        };
    }

    public static <T> Call<T> just(Throwable throwable) {
        return new Call<T>() {
            @Override
            public void enqueue(Action<T> onData, Action<Throwable> onError) {
                onError.call(throwable);
            }

            @Override
            public void cancel() {

            }
        };
    }
}
