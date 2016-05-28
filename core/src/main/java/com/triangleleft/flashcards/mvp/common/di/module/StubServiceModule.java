package com.triangleleft.flashcards.mvp.common.di.module;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.stub.StubFlashcardsModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.stub.StubLoginModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.stub.StubVocabularModule;
import com.triangleleft.flashcards.util.IPersistentStorage;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class StubServiceModule {

    @ApplicationScope
    @Provides
    public ILoginModule loginModule(@NonNull IPersistentStorage storage) {
        return new StubLoginModule(storage);
    }

    @ApplicationScope
    @Provides
    public IVocabularModule vocabularModule() {
        return new StubVocabularModule();
    }

    @ApplicationScope
    @Provides
    public IFlashcardsModule flashcardsModule() {
        return new StubFlashcardsModule();
    }
}
