package com.triangleleft.flashcards.ui.common.di;

import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.account.SimpleAccountModule;
import com.triangleleft.flashcards.service.vocabular.DbVocabularyWordsRepository;
import com.triangleleft.flashcards.service.vocabular.VocabularyWordsRepository;
import com.triangleleft.flashcards.ui.common.di.scope.ApplicationScope;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ComponentModule {

    @ApplicationScope
    @Binds
    public abstract VocabularyWordsRepository vocabularWordsCache(DbVocabularyWordsRepository cache);

    @ApplicationScope
    @Binds
    public abstract AccountModule accountModule(SimpleAccountModule simpleAccountModule);
}
