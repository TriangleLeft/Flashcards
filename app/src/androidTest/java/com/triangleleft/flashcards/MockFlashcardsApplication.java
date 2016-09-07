package com.triangleleft.flashcards;

import com.triangleleft.flashcards.di.AndroidApplicationComponent;
import com.triangleleft.flashcards.di.AndroidApplicationModule;
import com.triangleleft.flashcards.di.DaggerAndroidApplicationComponent;
import com.triangleleft.flashcards.di.NetworkModule;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import okhttp3.HttpUrl;

public class MockFlashcardsApplication extends FlashcardsApplication {

    private static final Logger logger = LoggerFactory.getLogger(MockFlashcardsApplication.class);

    @NonNull
    @Override
    protected AndroidApplicationComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerAndroidApplicationComponent.builder()
                .androidApplicationModule(new AndroidApplicationModule(this))
                .retrofitModule(new NetworkModule() {
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
