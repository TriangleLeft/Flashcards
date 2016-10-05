package com.triangleleft.flashcards.util;

import com.google.gson.GsonBuilder;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class Utils {

    Matcher<Iterable<?>> contains = Matchers.contains(new Object());


    private Utils() {
        GsonBuilder builder = null;
    }

    public static void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
