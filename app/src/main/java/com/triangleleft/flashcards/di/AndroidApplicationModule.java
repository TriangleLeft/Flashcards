package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.vocabular.DbVocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.VocabularySQLiteOpenHelper;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.ui.common.FlagImagesProvider;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;
import com.triangleleft.flashcards.ui.common.SharedPreferencesPersistentStorage;
import com.triangleleft.flashcards.util.PersistentStorage;

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

    @ApplicationScope
    @Provides
    public Context context() {
        return application;
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
}
