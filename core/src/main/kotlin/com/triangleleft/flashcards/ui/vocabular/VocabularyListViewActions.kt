package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewAction

object VocabularyListViewActions {

    fun showList(list: List<VocabularyWord>): ViewAction<VocabularyListViewState> {
        return ViewAction { state ->
            // So, we also reset refresh status here
            state.copy(page = VocabularyListViewState.Page.Content(list), hasRefreshError = false,
                    isRefreshing = false)
        }
    }

    fun progress(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(page = VocabularyListViewState.Page.Progress) }
    }

    fun error(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(page = VocabularyListViewState.Page.Error) }
    }

    fun selectWord(position: Int): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(selectedPosition = position) }
    }

    fun refreshError(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(hasRefreshError = true) }
    }

    fun refreshProgress(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(isRefreshing = true) }
    }
}