package com.triangleleft.flashcards.ui.main;

import com.triangleleft.flashcards.ui.common.di.scope.ActivityScope;
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
