package com.triangleleft.flashcards;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public class Actions {
    public static <T> Action<T> empty() {
        return data -> {
        };
    }
}