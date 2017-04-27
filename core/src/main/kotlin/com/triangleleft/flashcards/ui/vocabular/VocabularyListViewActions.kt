package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewAction

object VocabularyListViewActions {

    fun showList(list: List<VocabularyWord>): ViewAction<VocabularyListViewState> {
        return ViewAction { state ->
            val page = state.page
            val selectedPosition = when (page) {
                is VocabularyListViewState.Page.Content -> page.selectedPosition
                else -> 0
            }
            // So, we also reset refresh status here
            state.copy(page = VocabularyListViewState.Page.Content(list, selectedPosition), hasRefreshError = false,
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
        return ViewAction { state ->
            // We assume we can select word only for content page
            // should we pass list as argument?
            check(state.page is VocabularyListViewState.Page.Content, { "Content page expected" })
            val content = state.page as VocabularyListViewState.Page.Content
            state.copy(page = VocabularyListViewState.Page.Content(content.list, position))
        }
    }

    fun refreshError(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(hasRefreshError = true) }
    }

    fun refreshProgress(): ViewAction<VocabularyListViewState> {
        return ViewAction { state -> state.copy(isRefreshing = true) }
    }

}