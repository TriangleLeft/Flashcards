package com.triangleleft.flashcards.mvp.vocabular.di;

import com.triangleleft.flashcards.android.vocabular.VocabularWordFragment;
import com.triangleleft.flashcards.dagger.component.IComponent;
import com.triangleleft.flashcards.dagger.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularWordPresenter;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class, modules = VocabularWordModule.class)
public interface VocabularWordComponent extends IComponent {

    IVocabularWordPresenter vocabularWordPresenter();

    void inject(VocabularWordFragment wordView);
}
