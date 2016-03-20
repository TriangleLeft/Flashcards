package com.triangleleft.flashcards.mvp.vocabular.di;

import com.triangleleft.flashcards.android.vocabular.VocabularListFragment;
import com.triangleleft.flashcards.dagger.component.IComponent;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenter;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class, modules = VocabularListModule.class)
public interface VocabularListComponent extends MainPageComponent, IComponent {

    IVocabularListPresenter vocabularListPresenter();

    void inject(VocabularListFragment vocabularListView);
}
