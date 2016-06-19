package com.triangleleft.flashcards.common.di;


import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.triangleleft.flashcards.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.common.FlagImagesProvider;
import com.triangleleft.flashcards.common.FlashcardsApplication;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.util.PersistentStorage;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public class ApplicationModule {
    private final FlashcardsApplication application;

    public ApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @ApplicationScope
    @Provides
    public Context context() {
        return application;
    }

    @ApplicationScope
    @Provides
    public PersistentStorage persistentStorage(Context context) {
        return new SharedPreferencesPersistentStorage(context);
    }

    @ApplicationScope
    @Provides
    public ComponentManager presenterManager() {
        return new ComponentManager(10, 1, TimeUnit.MINUTES);
    }

    @ApplicationScope
    @Provides
    public CookieJar cookieJar(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    @ApplicationScope
    @Provides
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @ApplicationScope
    @Provides
    public FlagImagesProvider flagImagesProvider(Context context) {
        return new FlagImagesProvider(context);
    }

}
