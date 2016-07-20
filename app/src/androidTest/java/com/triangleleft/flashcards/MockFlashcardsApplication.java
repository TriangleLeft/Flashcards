package com.triangleleft.flashcards;

import android.support.annotation.NonNull;

import com.triangleleft.flashcards.ui.common.FlashcardsApplication;
import com.triangleleft.flashcards.ui.common.di.ApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.ApplicationModule;
import com.triangleleft.flashcards.ui.common.di.DaggerApplicationComponent;
import com.triangleleft.flashcards.ui.common.di.module.NetModule;
import okhttp3.HttpUrl;
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
