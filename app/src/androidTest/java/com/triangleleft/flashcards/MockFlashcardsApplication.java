package com.triangleleft.flashcards;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.ApplicationModule;
import com.triangleleft.flashcards.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.ui.FlashcardsApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockFlashcardsApplication extends FlashcardsApplication {

    private static final Logger logger = LoggerFactory.getLogger(MockFlashcardsApplication.class);

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static FlashcardsApplication getInstance() {
        return debugInstance;
    }
}
