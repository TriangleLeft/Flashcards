package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.ui.common.view.IView

interface IVocabularyWordView : IView {

    fun render(state: VocabularyWordViewState)
}
