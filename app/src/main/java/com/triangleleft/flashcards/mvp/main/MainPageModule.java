package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPageModule {

    @ActivityScope
    @Provides
    public IVocabularNavigator vocabularNavigator(MainPresenter presenter) {
        return presenter;
    }
}
