package com.triangleleft.flashcards;

public interface Action<T> {
    void call(T t);
}