package com.triangleleft.flashcards;

public interface Observer<T> {

    void onError(Throwable throwable);

    void onNext(T data);

}
