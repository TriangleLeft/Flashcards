package com.triangleleft.flashcards;

public class Observers {
    public static <T> Observer<T> from(Action<T> onData, Action<Throwable> onError) {
        return new Observer<T>() {
            @Override
            public void onError(Throwable throwable) {
                onError.call(throwable);
            }

            @Override
            public void onNext(T data) {
                onData.call(data);
            }
        };
    }
}
