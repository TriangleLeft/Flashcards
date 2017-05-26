package com.triangleleft.flashcards.ui.vocabular.list

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView
import java.io.Serializable

interface VocabularyListView : IView<VocabularyListView.State, VocabularyListView.Event> {

    sealed class Event : ViewEvent {
        object Refresh : Event()
        object Load : Event()
        data class WordSelect(val word: VocabularyWord, val position: Int) : Event()
    }

    data class State(
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
}
