package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.AndroidApplicationScope;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public class AndroidApplicationModule {
    private final FlashcardsApplication application;

    public AndroidApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @AndroidApplicationScope
    @Provides
    public Context context() {
        return application;
    }

    @AndroidApplicationScope
    @Provides
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @AndroidApplicationScope
    @Provides
    public FlagImagesProvider flagImagesProvider(Context context) {
        return new FlagImagesProvider(context);
    }
}
