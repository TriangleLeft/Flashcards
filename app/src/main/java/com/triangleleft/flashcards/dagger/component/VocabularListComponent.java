package com.triangleleft.flashcards.dagger.component;

import com.triangleleft.flashcards.android.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.dagger.module.VocabularListModule;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenter;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainActivityComponent.class, modules = VocabularListModule.class)
public interface VocabularListComponent extends IComponent {

    IVocabularListPresenter vocabularListPresenter();

    void inject(VocabularListFragment vocabularListFragment);
}
