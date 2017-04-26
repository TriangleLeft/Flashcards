package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.login.ViewState

data class MainViewState(val title: String, val flashcardsDialogIsShown: Boolean, val page: Page) : ViewState {

    sealed class Page {
        object List : Page()
        object Exit : Page()
        data class Word(val word: VocabularyWord?) : Page()
    }

}