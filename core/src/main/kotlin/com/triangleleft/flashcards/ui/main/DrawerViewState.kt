package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.ViewState

data class DrawerViewState(val page: IDrawerView.Page, val username: String, val avatar: String,
                           val languages: List<Language>, val hasError: Boolean)
    : ViewState