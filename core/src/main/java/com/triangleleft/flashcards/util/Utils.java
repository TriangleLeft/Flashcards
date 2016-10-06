package com.triangleleft.flashcards.util;

import com.google.gson.GsonBuilder;

//import org.hamcrest.collection.IsIterableContainingInAnyOrder;

public class Utils {

    //IsIterableContainingInAnyOrder whatever = new IsIterableContainingInAnyOrder<Void>(null);

    private Utils() {
        GsonBuilder builder = null;
    }

    public static void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
