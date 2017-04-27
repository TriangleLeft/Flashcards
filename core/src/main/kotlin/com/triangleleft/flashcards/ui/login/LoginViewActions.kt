package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.ui.ViewAction
import com.triangleleft.flashcards.util.TextUtils
import org.slf4j.LoggerFactory

object LoginViewActions {

    private val logger = LoggerFactory.getLogger(LoginViewActions::class.java.simpleName)

    fun setLogin(login: String): ViewAction<LoginViewState> {
        logger.debug("setLogin {}", login)
        return ViewAction { it.copy(login = login, loginButtonEnabled = isLoginButtonEnabled(login, it.password)) }
    }

    fun setPassword(password: String): ViewAction<LoginViewState> {
        logger.debug("setPassword {}", password)
        return ViewAction { it.copy(password = password, loginButtonEnabled = isLoginButtonEnabled(it.login, password)) }
    }

    private fun isLoginButtonEnabled(login: String, password: String): Boolean {
        logger.debug("isLoginButtonEnabled: {} {}", login, password)
        return TextUtils.hasText(login) && TextUtils.hasText(password)
    }

    fun setRememberUser(rememberUser: Boolean): ViewAction<LoginViewState> {
        return ViewAction { it.copy(shouldRememberUser = rememberUser) }
    }

    fun loginError(): ViewAction<LoginViewState> {
        return ViewAction { it.copy(page = LoginViewState.Page.CONTENT, hasLoginError = true) }
    }

    fun passwordError(): ViewAction<LoginViewState> {
        return ViewAction { it.copy(page = LoginViewState.Page.CONTENT, hasPasswordError = true) }
    }

    fun genericError(genericError: String): ViewAction<LoginViewState> {
        return ViewAction { it.copy(page = LoginViewState.Page.CONTENT, genericError = genericError) }
    }

    fun success(): ViewAction<LoginViewState> {
        return ViewAction { it.copy(page = LoginViewState.Page.SUCCESS) }
    }

    fun progress(): ViewAction<LoginViewState> {
        return ViewAction { it.copy(page = LoginViewState.Page.PROGRESS) }
    }
}
