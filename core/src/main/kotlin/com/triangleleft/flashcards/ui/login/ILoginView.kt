package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.ui.common.view.IView

import io.reactivex.Observable

/**
 * View interface for login screen.
 */
interface ILoginView : IView {

    fun render(viewState: LoginViewState)

    fun logins(): Observable<String>

    fun passwords(): Observable<String>

    fun rememberUserChecks(): Observable<Boolean>

    fun loginEvents(): Observable<LoginEvent>
}
