package com.triangleleft.flashcards.mvp.common.di.module;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.IDuolingoRest;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.rest.RestFlashcardsModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.rest.RestVocabularModule;
import com.triangleleft.flashcards.util.IPersistentStorage;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class RestServiceModule {

    @ApplicationScope
    @Provides
    public ILoginModule loginModule(@NonNull IDuolingoRest service, @NonNull IPersistentStorage storage) {
        return new RestLoginModule(service, storage);
    }

    @ApplicationScope
    @Provides
    public IVocabularModule vocabularModule(@NonNull IDuolingoRest service) {
        return new RestVocabularModule(service);
    }

    @ApplicationScope
    @Provides
    public IFlashcardsModule flashcardsModule(@NonNull IDuolingoRest service) {
        return new RestFlashcardsModule(service);
    }
}
