package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.login.LoginModule
import com.triangleleft.flashcards.service.login.exception.LoginException
import com.triangleleft.flashcards.service.login.exception.PasswordException
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Observable
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

        // Transform login button click into login request
        val loginTransformer = ObservableTransformer<LoginClickEvent, ViewAction<LoginViewState>> { upstream ->
            upstream.flatMap { (login, password) ->
                loginModule.login(login, password)
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

        // Map each event to appropriate view action
        val transformer = ObservableTransformer<LoginViewEvent, ViewAction<LoginViewState>> {
            events ->
            events.publish { shared ->
                Observable.merge(
                        shared.ofType(SetLoginEvent::class.java).map { LoginViewActions.setLogin(it.login) },
                        shared.ofType(SetPasswordEvent::class.java).map { LoginViewActions.setPassword(it.password) },
                        shared.ofType(SetRememberUserEvent::class.java)
                                .doOnNext { accountModule.setRememberUser(it.rememberUser) }
                                .map { LoginViewActions.setRememberUser(it.rememberUser) },
                        shared.ofType(LoginClickEvent::class.java).compose(loginTransformer)
                )
            }
        }
        setup(viewEvents().compose(transformer), state)
    }


}
