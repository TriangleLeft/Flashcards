package com.triangleleft.flashcards.ui.vocabular.di;

import com.triangleleft.flashcards.ui.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.ui.main.di.MainPageComponent;
import com.triangleleft.flashcards.ui.vocabular.VocabularyListFragment;
import com.triangleleft.flashcards.ui.vocabular.VocabularyListPresenter;
import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularyListComponent extends MainPageComponent {
    VocabularyListPresenter vocabularListPresenter();

    void inject(VocabularyListFragment vocabularyListFragment);
}
