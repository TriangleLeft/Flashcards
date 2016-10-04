package com.triangleleft.flashcards;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface Action<T> {
    void call(T data);
}