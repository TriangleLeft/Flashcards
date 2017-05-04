package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.ui.ViewEvent


sealed class LoginViewEvent : ViewEvent

data class SetLoginEvent(val login: String) : LoginViewEvent()

data class SetPasswordEvent(val password: String) : LoginViewEvent()

data class SetRememberUserEvent(val rememberUser: Boolean) : LoginViewEvent()

object GenericErrorShownEvent : LoginViewEvent()

data class LoginClickEvent(val login: String, val password: String) : LoginViewEvent()
