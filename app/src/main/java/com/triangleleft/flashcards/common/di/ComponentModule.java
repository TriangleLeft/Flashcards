package com.triangleleft.flashcards.common.di;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.account.SimpleAccountModule;
import com.triangleleft.flashcards.service.vocabular.DbVocabularyWordsCache;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsCache;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ComponentModule {

    @ApplicationScope
    @Binds
    public abstract VocabularyWordsCache vocabularWordsCache(DbVocabularyWordsCache cache);

    @ApplicationScope
    @Binds
    public abstract AccountModule accountModule(SimpleAccountModule simpleAccountModule);
}
