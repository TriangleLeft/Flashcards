package com.triangleleft.flashcards.mvp.common.di.module;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.cards.IFlashcardsModule;
import com.triangleleft.flashcards.service.cards.rest.RestFlashcardsModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.rest.RestLoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.rest.RestSettingsModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.rest.RestVocabularyModule;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RestServiceModule {

    @ApplicationScope
    @Binds
    abstract LoginModule loginModule(RestLoginModule module);

    @ApplicationScope
    @Binds
    abstract VocabularyModule vocabularModule(RestVocabularyModule module);

    @ApplicationScope
    @Binds
    abstract IFlashcardsModule flashcardsModule(RestFlashcardsModule module);

    @ApplicationScope
    @Binds
    abstract SettingsModule settingsModule(RestSettingsModule module);
}
