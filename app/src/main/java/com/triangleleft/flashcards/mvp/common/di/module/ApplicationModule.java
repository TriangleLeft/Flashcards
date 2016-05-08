package com.triangleleft.flashcards.mvp.common.di.module;

import com.triangleleft.flashcards.android.FlashcardsApplication;
import com.triangleleft.flashcards.android.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.util.IPersistentStorage;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final FlashcardsApplication application;

    public ApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @ApplicationScope
    @Provides
    public FlashcardsApplication application() {
        return application;
    }

    @ApplicationScope
    @Provides
    public IPersistentStorage persistentStorage(FlashcardsApplication application) {
        return new SharedPreferencesPersistentStorage(application);
    }

    @ApplicationScope
    @Provides
    public ComponentManager presenterManager() {
        return new ComponentManager(10, 1, TimeUnit.MINUTES);
    }
}
