package com.triangleleft.flashcards;

import com.triangleleft.flashcards.di.AndroidApplicationModule;
import com.triangleleft.flashcards.di.ApplicationComponent;
import com.triangleleft.flashcards.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.di.NetModule;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import okhttp3.HttpUrl;

public class MockFlashcardsApplication extends FlashcardsApplication {

    private static final Logger logger = LoggerFactory.getLogger(MockFlashcardsApplication.class);

    @NonNull
    @Override
    protected ApplicationComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerApplicationComponent.builder()
                .applicationModule(new AndroidApplicationModule(this))
                .netModule(new NetModule() {
                    @Override
                    public HttpUrl endpoint() {
                        return MockWebServerRule.MOCK_SERVER_URL;
                    }
                })
                .build();
    }

    public static FlashcardsApplication getInstance() {
        return debugInstance;
    }
}
