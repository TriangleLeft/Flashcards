package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent

sealed class VocabularyListEvent : ViewEvent {
    object Refresh : VocabularyListEvent()
    object Load : VocabularyListEvent()
    data class WordSelect(val word: VocabularyWord, val position: Int) : VocabularyListEvent()
}