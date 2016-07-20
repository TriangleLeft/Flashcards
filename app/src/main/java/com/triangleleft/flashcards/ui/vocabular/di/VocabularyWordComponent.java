package com.triangleleft.flashcards.ui.vocabular.di;

import com.triangleleft.flashcards.ui.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.ui.main.di.MainPageComponent;
import com.triangleleft.flashcards.ui.vocabular.VocabularyWordFragment;
import com.triangleleft.flashcards.ui.vocabular.VocabularyWordPresenter;
import dagger.Component;

@FragmentScope
@Component(dependencies = MainPageComponent.class)
public interface VocabularyWordComponent extends MainPageComponent {

    VocabularyWordPresenter vocabularWordPresenter();

    void inject(VocabularyWordFragment vocabularyWordFragment);
}
