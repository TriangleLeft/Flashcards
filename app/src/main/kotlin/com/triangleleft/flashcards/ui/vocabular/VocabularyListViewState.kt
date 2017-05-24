package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewState
import java.io.Serializable

data class VocabularyListViewState(
        val page: Page,
        val selectedPosition: Int,
        val hasRefreshError: Boolean,
        val isRefreshing: Boolean)
    : ViewState {

    sealed class Page : Serializable {
        data class Content(val list: List<VocabularyWord>) : Page() {
            override fun toString(): String {
                return Content::class.java.simpleName
            }
        }

        object Progress : Page()
        object Error : Page()
    }
}