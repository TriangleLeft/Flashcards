package com.triangleleft.flashcards.ui.login

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.login.LoginModule
import com.triangleleft.flashcards.service.login.exception.LoginException
import com.triangleleft.flashcards.service.login.exception.PasswordException
import com.triangleleft.flashcards.ui.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named


@ActivityScope
class LoginPresenter @Inject
constructor(private val accountModule: AccountModule, private val loginModule: LoginModule,
            @Named(AbstractPresenter.VIEW_EXECUTOR) executor: Executor,
            @Named(AbstractPresenter.UI_SCHEDULER) private val uiScheduler: Scheduler)
    : AbstractPresenter<ILoginView, LoginViewState>(ILoginView::class.java, executor) {

    companion object {
        private val logger = LoggerFactory.getLogger(LoginPresenter::class.java)
    }

    private val logins = PublishSubject.create<String>()
    private val passwords = PublishSubject.create<String>()
    private val rememberUserChecks = PublishSubject.create<Boolean>()
    private val loginEvents = PublishSubject.create<LoginEvent>()
    private val viewStates = BehaviorSubject.create<LoginViewState>()

    private var disposable = CompositeDisposable()

    // If we are already logged, and we have saved user data, advance immediately
    private val initialState: LoginViewState
        get() {
            val login = accountModule.login.orElse("")
            val rememberUser = accountModule.shouldRememberUser()
            val isLogged = rememberUser && accountModule.userData.isPresent && accountModule.userId.isPresent
            val page = if (isLogged) LoginViewState.Page.SUCCESS else LoginViewState.Page.CONTENT

            return LoginViewState(login, "", false, false, "", false, rememberUser, page)
        }

    override fun getViewState(): LoginViewState {
        return viewStates.value
    }

    override fun onCreate(savedViewState: LoginViewState?) {
        logger.debug("onCreate() called with savedViewState = [{}]", savedViewState)

        val initialState = savedViewState ?: initialState

        val loginTransformer = ObservableTransformer<LoginEvent, ViewAction<LoginViewState>> { upstream ->
            upstream.flatMap { (login, password) ->
                loginModule.login(login, password)
                        .map { _ -> LoginViewActions.success() }
                        .onErrorReturn {
                            when (it) {
                                is LoginException -> LoginViewActions.loginError()
                                is PasswordException -> LoginViewActions.passwordError()
                                else -> LoginViewActions.genericError("TODO: error message")
                            }
                        }
                        .startWith(LoginViewActions.progress())
            }
        }

        Observable.merge(
                logins.map { LoginViewActions.setLogin(it) },
                passwords.map { LoginViewActions.setPassword(it) },
                rememberUserChecks.doOnNext { accountModule.setRememberUser(it) }.map { LoginViewActions.setRememberUser(it) },
                loginEvents.compose(loginTransformer))
                .scan(initialState) { viewState, viewAction -> viewAction.reduce(viewState) }
                .distinctUntilChanged()
                .observeOn(uiScheduler)
                .subscribe { state ->
                    logger.debug("onNext() {}", state)
                    viewStates.onNext(state)
                }

        viewStates.onNext(initialState)
    }

    @Synchronized override fun onRebind(view: ILoginView) {
        super.onRebind(view)

        disposable = CompositeDisposable()
        // Connect view observables to our publish subjects
        // Connect view to our behavior subject
        disposable.addAll(
                viewStates.subscribe { view.render(it) },
                view.logins().subscribe { logins.onNext(it) },
                view.passwords().subscribe { passwords.onNext(it) },
                view.rememberUserChecks().subscribe { rememberUserChecks.onNext(it) },
                view.loginEvents().subscribe { loginEvents.onNext(it) }
        )
    }

    override fun onUnbind() {
        super.onUnbind()

        disposable.dispose()
    }

    override fun onDestroy() {
        logger.debug("onDestroy() called")
    }
}
