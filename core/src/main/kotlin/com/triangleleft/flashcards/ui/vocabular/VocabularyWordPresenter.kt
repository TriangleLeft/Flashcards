package com.triangleleft.flashcards.ui.vocabular

import com.annimon.stream.Optional
import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class VocabularyWordPresenter @Inject
constructor(@Named(AbstractPresenter.VIEW_EXECUTOR) executor: Executor) : AbstractPresenter<IVocabularyWordView, ViewState>(IVocabularyWordView::class.java, executor) {

    fun showWord(word: Optional<VocabularyWord>) {
        if (word.isPresent) {
            applyState { view -> view.showWord(word.get()) }
        } else {
            applyState { it.showEmpty() }
        }
    }
}
