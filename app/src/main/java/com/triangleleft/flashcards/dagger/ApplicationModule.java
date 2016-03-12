package com.triangleleft.flashcards.dagger;

import com.triangleleft.flashcards.FlashcardsApplication;
import com.triangleleft.flashcards.dagger.scope.ApplicationScope;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;

import android.content.Context;
import android.content.SharedPreferences;

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
    public SharedPreferences preferences() {
        return application.getSharedPreferences(ApplicationModule.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @ApplicationScope
    @Provides
    public ComponentManager presenterManager() {
        return new ComponentManager(10, 1, TimeUnit.MINUTES);
    }
}
