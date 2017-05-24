package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.settings.SettingsModule
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@FunctionsAreNonnullByDefault
@ActivityScope
class DrawerPresenter @Inject constructor(
        private val mainPresenter: MainPresenter,
        private val accountModule: AccountModule,
        private val settingsModule: SettingsModule,
        @Named(AbstractRxPresenter.UI_SCHEDULER) scheduler: Scheduler)
    : AbstractRxPresenter<DrawerView, DrawerViewState, ViewEvent>(scheduler) {

    var currentLanguage: Language? = null
    private val scheduler: Scheduler? = null
    private val languageClicks = PublishSubject.create<Language>()
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val initialState: DrawerViewState

    init {
        val userData = accountModule.userData.get()
        initialState = DrawerViewState(DrawerView.Page.CONTENT, userData.username, userData.avatar, userData.sortedLanguages, false)
    }

    override fun onCreate(savedViewState: DrawerViewState?) {
        super.onCreate(savedViewState)

        val state = initialState

        // transform click on language into request to switch to it
        // ignore clicks on current language
        // start with progress
        // on error display toast with error message
        val languageClickTransformer = ObservableTransformer<Language, ViewAction<DrawerViewState>> { upstream ->
            upstream.flatMap { language ->
                if (language != currentLanguage) {
                    settingsModule.switchLanguage(language)
                            .map { _ -> DrawerViewActions.content() }
                            .startWith { DrawerViewActions.progress() }
                            .onErrorReturn { DrawerViewActions.error() }
                } else {
                    Observable.empty()
                }
            }
        }

        Observable.merge(
                languageClicks.compose(languageClickTransformer),
                settingsModule.userData().map { data -> DrawerViewActions.showData(data) }
        ).scan(state) { state, action -> action.reduce(state) }
                .distinctUntilChanged()
                .subscribe { viewStates.onNext(it) }
    }


    companion object {

        private val logger = LoggerFactory.getLogger(DrawerPresenter::class.java)
    }
}
