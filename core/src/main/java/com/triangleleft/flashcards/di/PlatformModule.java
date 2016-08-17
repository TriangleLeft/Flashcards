package com.triangleleft.flashcards.di;

import com.triangleleft.flashcards.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.ui.FlashcardsNavigator;
import com.triangleleft.flashcards.util.PersistentStorage;

import dagger.Module;
import dagger.Provides;

@Module
public class PlatformModule {

    private final PersistentStorage storage;
    private final VocabularyWordsRepository repository;
    private final RestService service;
    private final FlashcardsNavigator navigator;

    public PlatformModule(FlashcardsNavigator navigator, PersistentStorage storage,
                          VocabularyWordsRepository repository, RestService service) {
        this.navigator = navigator;
        this.storage = storage;
        this.repository = repository;
        this.service = service;
    }

    @ApplicationScope
    @Provides
    public FlashcardsNavigator navigator() {
        return navigator;
    }

    @ApplicationScope
    @Provides
    public PersistentStorage persistentStorage() {
        return storage;
    }

    @ApplicationScope
    @Provides
    public VocabularyWordsRepository vocabularWordsCache() {
        return repository;
    }

    @ApplicationScope
    @Provides
    public RestService restService() {
        return service;
    }
}
