package com.triangleleft.flashcards.util;

import timber.log.Timber;

public class SystemOutTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        System.out.println(tag + ": " + message);
    }
}
