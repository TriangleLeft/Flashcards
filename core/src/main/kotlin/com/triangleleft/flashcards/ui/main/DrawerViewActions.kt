package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.settings.UserData
import com.triangleleft.flashcards.ui.ViewAction

object DrawerViewActions {
    fun progress(): ViewAction<DrawerViewState> {
        return ViewAction { state -> state.copy(page = DrawerView.Page.PROGRESS) }
    }

    fun content(): ViewAction<DrawerViewState> {
        return ViewAction { state -> state.copy(page = DrawerView.Page.CONTENT) }
    }

    fun error(): ViewAction<DrawerViewState>? {
        return ViewAction { state -> state.copy(hasError = true) }
    }

    fun showData(data: UserData): ViewAction<DrawerViewState> {
        return ViewAction { state ->
            state.copy(username = data.username, avatar = data.avatar, languages = data.sortedLanguages)
        }
    }

}