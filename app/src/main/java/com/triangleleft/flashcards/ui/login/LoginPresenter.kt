package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.login.LoginModule
import com.triangleleft.flashcards.service.login.exception.LoginException
import com.triangleleft.flashcards.service.login.exception.PasswordException
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import com.triangleleft.flashcards.util.TextUtils
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named


@ActivityScope
class LoginPresenter
@Inject
constructor(
        private val accountModule: AccountModule,
        private val loginModule: LoginModule,
        @Named(AbstractRxPresenter.UI_SCHEDULER) uiScheduler: Scheduler)
    : AbstractRxPresenter<LoginView, LoginView.State, LoginView.Event>(uiScheduler) {

    companion object {
        private val logger = LoggerFactory.getLogger(LoginPresenter::class.java)
    }

    override fun onCreate(savedViewState: LoginView.State?) {
        logger.debug("onCreate() called with savedViewState = [{}]", savedViewState)

        val login = accountModule.login.orElse("")
        val rememberUser = accountModule.shouldRememberUser()
        val isLogged = rememberUser && accountModule.userData.isPresent && accountModule.userId.isPresent
        // If we are already logged, and we have saved user data, advance immediately
        val page = if (isLogged) LoginView.State.Page.SUCCESS else LoginView.State.Page.CONTENT
        val initialState: LoginView.State = LoginView.State(login, "", false, false, false, false, rememberUser, page)

        // So, if we have any savedViewState, use it, but always change to content page
        // as we don't want to be stuck on progress page
        val state = savedViewState?.copy(page = LoginView.State.Page.CONTENT) ?: initialState

        // Map each event to appropriate view action
        val transformer = ObservableTransformer<LoginView.Event, ViewAction<LoginView.State>> {
            events ->
            events.flatMap {
                when (it) {
                    is LoginView.Event.SetLoginEvent -> just(LoginViewActions.setLogin(it.login))
                    is LoginView.Event.SetPasswordEvent -> just(LoginViewActions.setPassword(it.password))
                    is LoginView.Event.SetRememberUserEvent -> {
                        accountModule.setRememberUser(it.rememberUser)
                        just(LoginViewActions.setRememberUser(it.rememberUser))
                    }
                    is LoginView.Event.GenericErrorShownEvent -> Observable.empty()
                    is LoginView.Event.LoginClickEvent -> login(it.login, it.password)
                }
            }
        }

        setup(viewEvents().compose(transformer), state)
    }

    fun login(login: String, password: String): Observable<ViewAction<LoginView.State>> {
        return loginModule.login(login, password)
                .map { _ -> LoginViewActions.success() }
                .startWith(LoginViewActions.progress())
                .onErrorReturn {
                    when (it) {
                        is LoginException -> LoginViewActions.loginError()
                        is PasswordException -> LoginViewActions.passwordError()
                        else -> LoginViewActions.genericError()
                    }
                }
    }

    private object LoginViewActions {

        fun setLogin(login: String): ViewAction<LoginView.State> {
            return ViewAction { it.copy(login = login, loginButtonEnabled = isLoginButtonEnabled(login, it.password)) }
        }

        fun setPassword(password: String): ViewAction<LoginView.State> {
            return ViewAction { it.copy(password = password, loginButtonEnabled = isLoginButtonEnabled(it.login, password)) }
        }

        private fun isLoginButtonEnabled(login: String, password: String): Boolean {
            return TextUtils.hasText(login) && TextUtils.hasText(password)
        }

        fun setRememberUser(rememberUser: Boolean): ViewAction<LoginView.State> {
            return ViewAction { it.copy(shouldRememberUser = rememberUser) }
        }

        fun loginError(): ViewAction<LoginView.State> {
            return ViewAction { it.copy(page = LoginView.State.Page.CONTENT, hasLoginError = true) }
        }

        fun passwordError(): ViewAction<LoginView.State> {
            return ViewAction { it.copy(page = LoginView.State.Page.CONTENT, hasPasswordError = true) }
        }

        fun genericError(): ViewAction<LoginView.State> {
            return ViewAction { it.copy(page = LoginView.State.Page.CONTENT, hasGenericError = true) }
        }

        fun success(): ViewAction<LoginView.State> {
            return ViewAction { it.copy(page = LoginView.State.Page.SUCCESS) }
        }

        fun progress(): ViewAction<LoginView.State> {
            return ViewAction { it.copy(page = LoginView.State.Page.PROGRESS) }
        }
    }

}
