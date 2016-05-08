package com.triangleleft.flashcards.vocabular.di;

import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.VocabularListPresenter;
import com.triangleleft.flashcards.vocabular.VocabularListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularListComponent extends MainPageComponent {
    VocabularListPresenter vocabularListPresenter();

    void inject(VocabularListFragment vocabularListFragment);
}
