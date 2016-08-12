package com.triangleleft.flashcards.util;

import com.google.gson.GsonBuilder;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;

public class Utils {


    private Utils() {
        TestSubscriber<Void> unused = null;
        IsIterableContainingInAnyOrder<Void> unused2 = null;
        GsonBuilder builder = null;
        //List<Class<?>> classes = Arrays.asList(MockRespons)


    }

    public static void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
