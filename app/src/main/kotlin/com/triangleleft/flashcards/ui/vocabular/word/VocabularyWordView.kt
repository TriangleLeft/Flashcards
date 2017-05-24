package com.triangleleft.flashcards.ui.vocabular.word

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView

interface VocabularyWordView : IView<VocabularyWordView.State, ViewEvent> {

    data class State(val word: VocabularyWord?) : ViewState

}
