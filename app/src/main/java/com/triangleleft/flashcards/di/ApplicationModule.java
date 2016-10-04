package com.triangleleft.flashcards.di;

import com.google.gson.Gson;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.account.SimpleAccountModule;
import com.triangleleft.flashcards.service.vocabular.DbVocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.VocabularySQLiteOpenHelper;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.rest.RestVocabularyModule;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.ui.common.ComponentManager;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;
import com.triangleleft.flashcards.ui.common.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.PersistentStorage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final FlashcardsApplication application;

    public ApplicationModule(FlashcardsApplication application) {
        this.application = application;
    }

    @Provides
    @Named(RestVocabularyModule.MAIN_EXECUTOR)
    public Executor singleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @ApplicationScope
    @Provides
    @Named(AbstractPresenter.VIEW_EXECUTOR)
    public Executor uiThreadExecutor(Handler handler) {
        return command -> {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                command.run();
            } else {
                handler.post(command);
            }
        };
    }

    @ApplicationScope
    @Provides
    public Handler mainThreadHandler() {
        return new Handler(Looper.getMainLooper());
    }

    @ApplicationScope
    @Provides
    public Context context() {
        return application;
    }

    @ApplicationScope
    @Provides
    public FlagImagesProvider flagImagesProvider(Context context) {
        return new FlagImagesProvider(context);
    }

    @ApplicationScope
    @Provides
    public FlashcardsNavigator navigator() {
        return application;
    }

    @ApplicationScope
    @Provides
    public PersistentStorage persistentStorage() {
        return new SharedPreferencesPersistentStorage(application);
    }

    @ApplicationScope
    @Provides
    public VocabularyWordsRepository vocabularWordsCache() {
        return new DbVocabularyWordsRepository(new VocabularySQLiteOpenHelper(application));
    }

    @ApplicationScope
    @Provides
    public ComponentManager componentManager() {
        return ComponentManager.buildDefault();
    }

    @ApplicationScope
    @Provides
    public AccountModule accountModule(SimpleAccountModule simpleAccountModule) {
        return simpleAccountModule;
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }


}
