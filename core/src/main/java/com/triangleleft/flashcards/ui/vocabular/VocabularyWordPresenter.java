package com.triangleleft.flashcards.ui.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.di.scope.FragmentScope;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
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
