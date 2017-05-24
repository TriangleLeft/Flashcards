package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.common.ViewAction

object MainViewActions {

    fun backress(): ViewAction<MainViewState> {
        return ViewAction {
            // Backing from list show exit, otherwise we should show list
            val nextPage = if (it.page is MainViewState.Page.List) MainViewState.Page.Exit else MainViewState.Page.List
            it.copy(page = nextPage)
        }
    }

    fun wordClick(word: VocabularyWord): ViewAction<MainViewState> {
        return ViewAction { it.copy(page = MainViewState.Page.Word(word)) }
    }
}