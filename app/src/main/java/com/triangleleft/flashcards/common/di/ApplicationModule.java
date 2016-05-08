package com.triangleleft.flashcards.common.di;


import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.triangleleft.flashcards.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.common.FlashcardsApplication;
import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.mvp.common.presenter.ComponentManager;
import com.triangleleft.flashcards.util.IPersistentStorage;

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

    @ApplicationScope
    @Provides
    public CookieJar cookieJar(FlashcardsApplication application) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application));
    }

    @ApplicationScope
    @Provides
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
