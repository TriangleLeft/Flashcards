package com.triangleleft.flashcards.vocabular.di;

import com.triangleleft.flashcards.mvp.vocabular.VocabularWordPresenter;
import com.triangleleft.flashcards.vocabular.VocabularWordFragment;
import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.main.di.MainPageComponent;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularWordComponent extends MainPageComponent {

    VocabularWordPresenter vocabularWordPresenter();

    void inject(VocabularWordFragment vocabularWordFragment);
}
