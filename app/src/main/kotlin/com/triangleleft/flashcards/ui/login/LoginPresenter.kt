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
class LoginPresenter @Inject
constructor(private val accountModule: AccountModule, private val loginModule: LoginModule,
            @Named(AbstractRxPresenter.UI_SCHEDULER) uiScheduler: Scheduler)
    : AbstractRxPresenter<LoginView, LoginViewState, LoginViewEvent>(uiScheduler) {

    companion object {
        private val logger = LoggerFactory.getLogger(LoginPresenter::class.java)
    }

    // If we are already logged, and we have saved user data, advance immediately
    private val initialState: LoginViewState
        get() {
            val login = accountModule.login.orElse("")
            val rememberUser = accountModule.shouldRememberUser()
            val isLogged = rememberUser && accountModule.userData.isPresent && accountModule.userId.isPresent
            val page = if (isLogged) LoginViewState.Page.SUCCESS else LoginViewState.Page.CONTENT

            return LoginViewState(login, "", false, false, false, false, rememberUser, page)
        }

    override fun onCreate(savedViewState: LoginViewState?) {
        logger.debug("onCreate() called with savedViewState = [{}]", savedViewState)

        // So, if we have any savedViewState, use it, but always change to content page
        // as we don't want to be stuck on progress page
        val state = savedViewState?.copy(page = LoginViewState.Page.CONTENT) ?: initialState

        // Map each event to appropriate view action
        val transformer = ObservableTransformer<LoginViewEvent, ViewAction<LoginViewState>> {
            events ->
            events.flatMap {
                when (it) {
                    is SetLoginEvent -> just(LoginViewActions.setLogin(it.login))
                    is SetPasswordEvent -> just(LoginViewActions.setPassword(it.password))
                    is SetRememberUserEvent -> {
                        accountModule.setRememberUser(it.rememberUser)
                        just(LoginViewActions.setRememberUser(it.rememberUser))
                    }
                    is GenericErrorShownEvent -> Observable.empty()
                    is LoginClickEvent -> loginModule.login(it.login, it.password)
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
            }
        }

        setup(viewEvents().compose(transformer), state)
    }

    private object LoginViewActions {

        fun setLogin(login: String): ViewAction<LoginViewState> {
            return ViewAction { it.copy(login = login, loginButtonEnabled = isLoginButtonEnabled(login, it.password)) }
        }

        fun setPassword(password: String): ViewAction<LoginViewState> {
            return ViewAction { it.copy(password = password, loginButtonEnabled = isLoginButtonEnabled(it.login, password)) }
        }

        private fun isLoginButtonEnabled(login: String, password: String): Boolean {
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

        fun genericError(): ViewAction<LoginViewState> {
            return ViewAction { it.copy(page = LoginViewState.Page.CONTENT, hasGenericError = true) }
        }

        fun success(): ViewAction<LoginViewState> {
            return ViewAction { it.copy(page = LoginViewState.Page.SUCCESS) }
        }

        fun progress(): ViewAction<LoginViewState> {
            return ViewAction { it.copy(page = LoginViewState.Page.PROGRESS) }
        }
    }

}
