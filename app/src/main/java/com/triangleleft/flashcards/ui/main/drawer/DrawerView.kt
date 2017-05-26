package com.triangleleft.flashcards.ui.main.drawer

import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView

interface DrawerView : IView<DrawerView.State, DrawerView.Event> {

    sealed class Event : ViewEvent {
        data class LanguageClick(val language: Language) : Event()
    }

    data class State(val page: DrawerView.Page,
                     val username: String,
                     val avatar: String,
                     val languages: List<Language>,
                     val hasError: Boolean) : ViewState

    enum class Page {
        CONTENT,
        PROGRESS
    }
}
