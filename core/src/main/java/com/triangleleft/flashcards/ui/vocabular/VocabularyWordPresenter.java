package com.triangleleft.flashcards.ui.vocabular;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.scope.FragmentScope;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.login.ViewState;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

@FunctionsAreNonnullByDefault
@FragmentScope
public class VocabularyWordPresenter extends AbstractPresenter<IVocabularyWordView, ViewState> {

    @Inject
    public VocabularyWordPresenter(@Named(VIEW_EXECUTOR) Executor executor) {
        super(IVocabularyWordView.class, executor);
    }

    public void showWord(Optional<VocabularyWord> word) {
        if (word.isPresent()) {
            applyState(view -> view.showWord(word.get()));
        } else {
            applyState(IVocabularyWordView::showEmpty);
        }
    }
}
