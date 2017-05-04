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
import javax.inject.Inject
import javax.inject.Named


@ActivityScope
class LoginPresenter @Inject
constructor(private val accountModule: AccountModule, private val loginModule: LoginModule,
            @Named(AbstractPresenter.UI_SCHEDULER) private val uiScheduler: Scheduler)
    : AbstractPresenter<LoginView, LoginViewState>() {

    companion object {
        private val logger = LoggerFactory.getLogger(LoginPresenter::class.java)
    }

    private val events = PublishSubject.create<LoginViewEvent>()
//    private val passwords = PublishSubject.create<String>()
//    private val rememberUserChecks = PublishSubject.create<Boolean>()
//    private val loginEvents = PublishSubject.create<LoginEvent>()

    private val viewStates = BehaviorSubject.create<LoginViewState>()

    private var disposable = CompositeDisposable()

    // If we are already logged, and we have saved user data, advance immediately
    private val initialState: LoginViewState
        get() {
            val login = accountModule.login.orElse("")
            val rememberUser = accountModule.shouldRememberUser()
            val isLogged = rememberUser && accountModule.userData.isPresent && accountModule.userId.isPresent
            val page = if (isLogged) LoginViewState.Page.SUCCESS else LoginViewState.Page.CONTENT

            return LoginViewState(login, "", false, false, false, false, rememberUser, page)
        }

    override fun getViewState(): LoginViewState {
        return viewStates.value
    }

    override fun onCreate(savedViewState: LoginViewState?) {
        logger.debug("onCreate() called with savedViewState = [{}]", savedViewState)

        // So, if we have any savedViewState, use it, but always change to content page
        // as we don't want to be stuck on progress page
        val initialState = savedViewState?.copy(page = LoginViewState.Page.CONTENT) ?: initialState

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

        // TODO: Can be moved to base class?
        events.compose(transformer)
                .scan(initialState) { viewState, viewAction -> viewAction.reduce(viewState) }
                .distinctUntilChanged()
                .observeOn(uiScheduler)
                .doOnNext { logger.debug("onNext() {}", it) }
                .subscribe { state ->
                    viewStates.onNext(state)
                }

        // Set initial state
        viewStates.onNext(initialState)
    }

    @Synchronized override fun onRebind(view: LoginView) {
        super.onRebind(view)

        // TODO: Could be moved to base class?
        disposable = CompositeDisposable()
        // Connect view observables to our publish subjects
        // Connect view to our behavior subject
        disposable.addAll(
                viewStates.subscribe { view.render(it) },
                view.events().subscribe { events.onNext(it) }
        )
    }

    override fun onUnbind() {
        super.onUnbind()

        disposable.dispose()
    }
}
