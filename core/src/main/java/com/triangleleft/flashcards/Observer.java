package com.triangleleft.flashcards;

public interface Observer<T> {

    void onError(Throwable e);

    void onNext(T t);

}
