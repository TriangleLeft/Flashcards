package com.triangleleft.flashcards;

public interface Call<T> {

    void enqueue(Action<T> onData, Action<Throwable> onError);

    void cancel();

}