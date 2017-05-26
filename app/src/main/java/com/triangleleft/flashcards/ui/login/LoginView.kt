package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView

/**
 * View interface for login screen.
 */
interface LoginView : IView<LoginView.State, LoginView.Event> {

    sealed class Event : ViewEvent {
        data class SetLoginEvent(val login: String) : Event()
        data class SetPasswordEvent(val password: String) : Event()
        data class SetRememberUserEvent(val rememberUser: Boolean) : Event()
        object GenericErrorShownEvent : Event()
        data class LoginClickEvent(val login: String, val password: String) : Event()
    }

    data class State(val login: String,
                     val password: String,
                     val hasLoginError: Boolean,
                     val hasPasswordError: Boolean,
                     val hasGenericError: Boolean,
                     val loginButtonEnabled: Boolean,
                     val shouldRememberUser: Boolean,
                     val page: Page) : ViewState {

        enum class Page {
            PROGRESS,
            CONTENT,
            SUCCESS
        }
    }
}
