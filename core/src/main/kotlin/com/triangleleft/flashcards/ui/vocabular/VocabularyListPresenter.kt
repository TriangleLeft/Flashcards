package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyModule
import com.triangleleft.flashcards.ui.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class VocabularyListPresenter @Inject
constructor(private val vocabularyModule: VocabularyModule, private val navigator: VocabularyNavigator,
            @Named(AbstractPresenter.VIEW_EXECUTOR) executor: Executor)
    : AbstractPresenter<IVocabularyListView, VocabularyListViewState>(IVocabularyListView::class.java, executor) {

    companion object {
        private val logger = LoggerFactory.getLogger(VocabularyListPresenter::class.java)
    }

    var disposable: CompositeDisposable = CompositeDisposable()
    private val refreshes = PublishSubject.create<Any>()
    private val wordSelections = PublishSubject.create<VocabularyWordSelectEvent>()
    private val loadListRetries = PublishSubject.create<Any>()
    private val viewStates = BehaviorSubject.create<VocabularyListViewState>()
    private val initialState: VocabularyListViewState

    init {
        initialState = VocabularyListViewState(VocabularyListViewState.Page.Progress, false, false)
    }

    override fun onCreate(savedViewState: VocabularyListViewState?) {
        logger.debug("onCreate() called")

        val state = savedViewState ?: initialState

        val refreshTransformer = ObservableTransformer<Any, ViewAction<VocabularyListViewState>> {
            upstream ->
            upstream.flatMap { _ ->
                vocabularyModule.refreshVocabularyWords()
                        .map { VocabularyListViewActions.showList(it) }
                        .startWith { VocabularyListViewActions.refreshProgress() }
                        .onErrorReturn { VocabularyListViewActions.refreshError() }
            }
        }

        val loadTransformer = ObservableTransformer<Any, ViewAction<VocabularyListViewState>> {
            upstream ->
            upstream.flatMap { _ ->
                vocabularyModule.loadVocabularyWords()
                        .map { VocabularyListViewActions.showList(it) }
                        .startWith(VocabularyListViewActions.progress())
                        .onErrorReturn { VocabularyListViewActions.error() }
            }
        }

        Observable.merge(
                refreshes.compose(refreshTransformer),
                loadListRetries.compose(loadTransformer),
                // This doOnNext looks ugly
                wordSelections.doOnNext { navigator.showWord(it.word) }
                        .map { VocabularyListViewActions.selectWord(it.position) })
                .scan(state) { state, action -> action.reduce(state) }
                .distinctUntilChanged()
                .subscribe { viewStates.onNext(it) }

        // Start load
        loadListRetries.onNext(Any())
    }

    override fun onRebind(view: IVocabularyListView) {
        super.onRebind(view)

        disposable = CompositeDisposable()
        disposable.addAll(
                viewStates.subscribe { view.render(it) },
                view.refreshes().subscribe { refreshes.onNext(it) },
                view.wordSelections().subscribe { wordSelections.onNext(it) }
        )
    }

    override fun onUnbind() {
        super.onUnbind()
        disposable.dispose()
    }

    fun onLoadList() {
        logger.debug("onLoadList() called")

        vocabularyModule.loadVocabularyWords()

    }

    fun onRefreshList() {
        logger.debug("onRefreshList()")

        vocabularyModule.refreshVocabularyWords()

    }
}

