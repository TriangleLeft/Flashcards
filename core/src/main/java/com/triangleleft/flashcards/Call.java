package com.triangleleft.flashcards;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface Call<T> {

    void enqueue(Action<T> onData, Action<Throwable> onError);

    void cancel();

}