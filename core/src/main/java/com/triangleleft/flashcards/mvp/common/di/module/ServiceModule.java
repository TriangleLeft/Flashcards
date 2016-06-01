package com.triangleleft.flashcards.mvp.common.di.module;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.rest.RestFlashcardsModule;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.stub.StubSettingsModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularModule;
import com.triangleleft.flashcards.service.vocabular.rest.RestVocabularModule;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @ApplicationScope
    @Provides
    public ILoginModule loginModule(RestLoginModule module) {
        return module;
    }

    @ApplicationScope
    @Provides
    public IVocabularModule vocabularModule(RestVocabularModule module) {
        return module;
    }

    @ApplicationScope
    @Provides
    public IFlashcardsModule flashcardsModule(RestFlashcardsModule module) {
        return module;
    }

    @ApplicationScope
    @Provides
    public ISettingsModule settingsModule(StubSettingsModule module) {
        return module;
    }
}
