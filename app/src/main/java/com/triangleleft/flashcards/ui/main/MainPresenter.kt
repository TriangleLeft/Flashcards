package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.di.main.MainPageModule
import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainPresenter
@Inject
constructor(
        private val accountModule: AccountModule,
        @Named(MainPageModule.WORD_SELECTIONS)
        private val wordSelections: Observable<VocabularyWord>,
        @Named(AbstractRxPresenter.UI_SCHEDULER)
        scheduler: Scheduler)
    : AbstractRxPresenter<MainView, MainView.State, ViewEvent>(scheduler) {

    private val backPresses = PublishSubject.create<Any>()

    override fun onCreate(savedViewState: MainView.State?) {

        val userData = accountModule.userData.get()
        // It's possible that we are using account that doesn't have any languages
        val title = userData.currentLearningLanguage?.name ?: ""
        val initialState = savedViewState ?: MainView.State(title, false, MainView.State.Page.List)

        val actions = Observable.merge(
                backPresses.map { MainViewActions.backpress() },
                wordSelections.map { MainViewActions.wordClick(it) })

        setup(actions, initialState)
    }

    fun onLanguageChanged(currentLanguage: Language) {
        logger.debug("onLanguageChanged() called with: currentLanguage = [{}]", currentLanguage)
//        title = currentLanguage.name
//        view.setTitle(title)
//        view.reloadList()
    }


    object MainViewActions {

        fun backpress(): ViewAction<MainView.State> {
            return ViewAction {
                // Backing from list show exit, otherwise we should show list
                val nextPage = if (it.page is MainView.State.Page.List) MainView.State.Page.Exit else MainView.State.Page.List
                it.copy(page = nextPage)
            }
        }

        fun wordClick(word: VocabularyWord): ViewAction<MainView.State> {
            return ViewAction { it.copy(page = MainView.State.Page.Word(word)) }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MainPresenter::class.java)
    }
}
