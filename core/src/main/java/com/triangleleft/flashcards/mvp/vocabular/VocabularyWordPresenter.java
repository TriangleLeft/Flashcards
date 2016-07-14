package com.triangleleft.flashcards.mvp.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.mvp.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
@FragmentScope
public class VocabularyWordPresenter extends AbstractPresenter<IVocabularyWordView> {

    @Inject
    public VocabularyWordPresenter() {
        super(IVocabularyWordView.class);
    }

    public void showWord(Optional<VocabularyWord> word) {
        if (word.isPresent()) {
            applyState(view -> view.showWord(word.get()));
        } else {
            applyState(IVocabularyWordView::showEmpty);
        }
    }
}
