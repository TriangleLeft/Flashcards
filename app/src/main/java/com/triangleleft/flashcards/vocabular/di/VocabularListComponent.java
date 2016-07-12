package com.triangleleft.flashcards.vocabular.di;

import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.vocabular.VocabularyListPresenter;
import com.triangleleft.flashcards.vocabular.VocabularyListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularListComponent extends MainPageComponent {
    VocabularyListPresenter vocabularListPresenter();

    void inject(VocabularyListFragment vocabularListFragment);
}
