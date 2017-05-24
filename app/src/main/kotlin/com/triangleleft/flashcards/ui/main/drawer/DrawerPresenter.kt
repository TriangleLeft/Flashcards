package com.triangleleft.flashcards.ui.main.drawer

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.settings.SettingsModule
import com.triangleleft.flashcards.service.settings.UserData
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import com.triangleleft.flashcards.ui.main.MainPresenter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@ActivityScope class DrawerPresenter @Inject constructor(
        private val mainPresenter: MainPresenter,
        private val accountModule: AccountModule,
        private val settingsModule: SettingsModule,
        @Named(UI_SCHEDULER) scheduler: Scheduler)
    : AbstractRxPresenter<DrawerView, DrawerView.State, DrawerView.Event>(scheduler) {

    var currentLanguage: Language? = null

    override fun onCreate(savedViewState: DrawerView.State?) {
        super.onCreate(savedViewState)

        val userData = accountModule.userData.get()
        val state = DrawerView.State(DrawerView.Page.CONTENT, userData.username, userData.avatar,
                userData.sortedLanguages, false)

        val transformer = ObservableTransformer<DrawerView.Event, ViewAction<DrawerView.State>> {
            events ->
            events.flatMap {
                when (it) {
                    is DrawerView.Event.LanguageClick -> languageClick(it.language)
                }
            }
        }

        val actions = Observable.merge(
                viewEvents().compose(transformer),
                settingsModule.userData().map { data -> Action.showData(data) }
        )

        setup(actions, state)
    }

    fun languageClick(language: Language): Observable<ViewAction<DrawerView.State>> {
        return if (language != currentLanguage) {
            settingsModule.switchLanguage(language)
                    .map { _ -> Action.content() }
                    .startWith { Action.progress() }
                    .onErrorReturn { Action.error() }
        } else {
            Observable.empty()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DrawerPresenter::class.java)
    }

    private object Action {
        fun progress(): ViewAction<DrawerView.State> {
            return ViewAction { state -> state.copy(page = DrawerView.Page.PROGRESS) }
        }

        fun content(): ViewAction<DrawerView.State> {
            return ViewAction { state -> state.copy(page = DrawerView.Page.CONTENT) }
        }

        fun error(): ViewAction<DrawerView.State>? {
            return ViewAction { state -> state.copy(hasError = true) }
        }

        fun showData(data: UserData): ViewAction<DrawerView.State> {
            return ViewAction { state ->
                state.copy(username = data.username, avatar = data.avatar, languages = data.sortedLanguages)
            }
        }

    }
}
