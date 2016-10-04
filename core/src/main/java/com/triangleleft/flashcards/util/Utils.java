package com.triangleleft.flashcards.util;

import com.google.gson.GsonBuilder;

public class Utils {


    private Utils() {
        //IsIterableContainingInAnyOrder<Void> unused2 = null;
        GsonBuilder builder = null;
        //List<Class<?>> classes = Arrays.asList(MockRespons)


    }

    public static void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
