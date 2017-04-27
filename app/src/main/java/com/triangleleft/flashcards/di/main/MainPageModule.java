package com.triangleleft.flashcards.di.main;

import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.ui.main.MainPresenter;
import com.triangleleft.flashcards.ui.vocabular.VocabularyNavigator;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPageModule {

    @ActivityScope
    @Provides
    public VocabularyNavigator vocabularNavigator(MainPresenter presenter) {
        return presenter;
    }
}
