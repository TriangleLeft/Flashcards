package com.triangleleft.flashcards.vocabular.di;

import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.vocabular.VocabularyWordPresenter;
import com.triangleleft.flashcards.vocabular.VocabularyWordFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularyWordComponent extends MainPageComponent {

    VocabularyWordPresenter vocabularWordPresenter();

    void inject(VocabularyWordFragment vocabularyWordFragment);
}
