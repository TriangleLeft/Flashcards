package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.common.view.IView

interface IVocabularyWordView : IView {
    fun showWord(word: VocabularyWord)

    fun showEmpty()
}
