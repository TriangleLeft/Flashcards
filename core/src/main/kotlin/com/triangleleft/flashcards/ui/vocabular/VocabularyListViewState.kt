package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewState

data class VocabularyListViewState(val page: Page, val hasRefreshError: Boolean, val isRefreshing: Boolean) : ViewState {

    sealed class Page {
        data class Content(val list: List<VocabularyWord>, val selectedPosition: Int) : Page()
        object Progress : Page()
        object Error : Page()
    }
}