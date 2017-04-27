package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.ui.ViewState

data class LoginViewState(val login: String, val password: String, val hasLoginError: Boolean,
                          val hasPasswordError: Boolean, val genericError: String, val loginButtonEnabled: Boolean,
                          val shouldRememberUser: Boolean, val page: Page)
    : ViewState {

    enum class Page {
        PROGRESS,
        CONTENT,
        SUCCESS
    }
}