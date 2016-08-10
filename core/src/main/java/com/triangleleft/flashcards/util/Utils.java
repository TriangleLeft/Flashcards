package com.triangleleft.flashcards.util;

public class Utils {

    private Utils() {}

    public static void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
