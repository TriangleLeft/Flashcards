package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import com.triangleleft.flashcards.ui.vocabular.word.VocabularyNavigator
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainPresenter @Inject constructor(
        accountModule: AccountModule,
        @Named(AbstractRxPresenter.UI_SCHEDULER) scheduler: Scheduler)
    : AbstractRxPresenter<IMainView, MainViewState, ViewEvent>(scheduler), VocabularyNavigator {


    private val wordClicks = PublishSubject.create<VocabularyWord>()
    private val backPresses = PublishSubject.create<Any>()
    private val initialState: MainViewState

    init {
        val userData = accountModule.userData.get()

        // It's possible that we are using account that doesn't have any languages
        val title = userData.currentLearningLanguage?.name ?: ""

        initialState = MainViewState(title, false, MainViewState.Page.List)
    }

    override fun onCreate(savedViewState: MainViewState?) {
        val initialState = savedViewState ?: initialState

        Observable.merge(
                backPresses.map { MainViewActions.backress() },
                wordClicks.map { MainViewActions.wordClick(it) })
                .scan(initialState) { state, action -> action.reduce(state) }
                .distinctUntilChanged()
                .subscribe { viewStates.onNext(it) }
    }

    override fun showWord(word: VocabularyWord) {
        wordClicks.onNext(word)
    }

    fun onLanguageChanged(currentLanguage: Language) {
        logger.debug("onLanguageChanged() called with: currentLanguage = [{}]", currentLanguage)
//        title = currentLanguage.name
//        view.setTitle(title)
//        view.reloadList()
    }


    private enum class Page {
        LIST,
        WORD
    }

    companion object {

        private val logger = LoggerFactory.getLogger(MainPresenter::class.java)
    }
}
