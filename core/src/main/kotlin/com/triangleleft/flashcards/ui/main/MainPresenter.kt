package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import com.triangleleft.flashcards.ui.vocabular.VocabularyNavigator
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject
constructor(accountModule: AccountModule)
    : AbstractPresenter<IMainView, MainViewState>(), VocabularyNavigator {


    private val wordClicks = PublishSubject.create<VocabularyWord>()
    private val backPresses = PublishSubject.create<Any>()
    private val viewStates = BehaviorSubject.create<MainViewState>()
    private var disposable = CompositeDisposable()
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

    override fun onRebind(view: IMainView) {
        super.onRebind(view)

        disposable = CompositeDisposable()
        // Connect view observables to our publish subjects
        // Connect view to our behavior subject
        disposable.addAll(
                viewStates.subscribe { view.render(it) },
                view.backPresses().subscribe { backPresses.onNext(it) },
                view.wordClicks().subscribe { wordClicks.onNext(it) }
        )
    }

    override fun onUnbind() {
        super.onUnbind()
        disposable.dispose()
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
