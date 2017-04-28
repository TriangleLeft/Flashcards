package com.triangleleft.flashcards.ui.vocabular

import com.annimon.stream.Optional
import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import javax.inject.Inject

@FragmentScope
class VocabularyWordPresenter @Inject
constructor() : AbstractPresenter<IVocabularyWordView, ViewState>() {

    fun showWord(word: Optional<VocabularyWord>) {
        if (word.isPresent) {
            view.showWord(word.get())
        } else {
            view.showEmpty()
        }
    }
}
