package com.triangleleft.flashcards.common.di;

import com.triangleleft.flashcards.mvp.common.di.scope.ApplicationScope;
import com.triangleleft.flashcards.service.vocabular.DbVocabularWordsCache;
import com.triangleleft.flashcards.service.vocabular.VocabularWordsCache;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ComponentModule {

    @ApplicationScope
    @Binds
    public abstract VocabularWordsCache vocabularWordsCache(DbVocabularWordsCache cache);

}
