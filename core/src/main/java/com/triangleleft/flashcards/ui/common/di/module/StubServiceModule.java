package com.triangleleft.flashcards.ui.common.di.module;

import com.triangleleft.flashcards.service.cards.FlashcardsModule;
import com.triangleleft.flashcards.service.cards.stub.StubFlashcardsModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.login.stub.StubLoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.stub.StubSettingsModule;
import com.triangleleft.flashcards.service.vocabular.VocabularyModule;
import com.triangleleft.flashcards.service.vocabular.stub.StubVocabularyModule;
import com.triangleleft.flashcards.ui.common.di.scope.ApplicationScope;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class StubServiceModule {

    @ApplicationScope
    @Binds
    abstract LoginModule loginModule(StubLoginModule module);

    @ApplicationScope
    @Binds
    abstract VocabularyModule vocabularModule(StubVocabularyModule module);

    @ApplicationScope
    @Binds
    abstract FlashcardsModule flashcardsModule(StubFlashcardsModule module);

    @ApplicationScope
    @Binds
    abstract SettingsModule settingsModule(StubSettingsModule module);
}
