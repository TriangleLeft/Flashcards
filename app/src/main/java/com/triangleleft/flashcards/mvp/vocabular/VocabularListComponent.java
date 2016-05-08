package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.android.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.main.MainPageComponent;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularListComponent extends MainPageComponent {
    VocabularListPresenter vocabularListPresenter();

    void inject(VocabularListFragment vocabularListFragment);
}
