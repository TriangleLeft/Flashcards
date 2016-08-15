package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.util.PersistentStorage;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;

@Module
public class PersistenceModule {

    private final PersistentStorage storage;
    private final CookieJar cookieJar;
    private final VocabularyWordsRepository repository;

    public PersistenceModule(PersistentStorage storage, CookieJar cookieJar, VocabularyWordsRepository repository) {
        this.storage = storage;
        this.cookieJar = cookieJar;
        this.repository = repository;
    }

    @ApplicationScope
    @Provides
    public PersistentStorage persistentStorage() {
        return storage;
    }

    @ApplicationScope
    @Provides
    public CookieJar cookieJar() {
        return cookieJar;
    }


    @ApplicationScope
    @Provides
    public VocabularyWordsRepository vocabularWordsCache() {
        return repository;
    }
}
