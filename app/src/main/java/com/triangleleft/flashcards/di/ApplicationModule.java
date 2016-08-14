package com.triangleleft.flashcards.di;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;
import com.triangleleft.flashcards.ui.common.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.ui.common.presenter.ComponentManager;
import com.triangleleft.flashcards.util.PersistentStorage;

import android.content.Context;

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
    public FlashcardsNavigator navigator() {
        return application;
    }

    @ApplicationScope
    @Provides
    public PersistentStorage persistentStorage(Context context) {
        return new SharedPreferencesPersistentStorage(context);
    }

    @ApplicationScope
    @Provides
    public ComponentManager componentManager() {
        return ComponentManager.buildDefault();
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
