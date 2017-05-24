package com.triangleleft.flashcards.di.vocabular;

import com.triangleleft.flashcards.di.main.MainPageComponent;
import com.triangleleft.flashcards.di.scope.FragmentScope;
import com.triangleleft.flashcards.ui.vocabular.list.VocabularyListFragment;
import com.triangleleft.flashcards.ui.vocabular.list.VocabularyListPresenter;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularyListComponent extends MainPageComponent {
    VocabularyListPresenter vocabularListPresenter();

    void inject(VocabularyListFragment vocabularyListFragment);
}
