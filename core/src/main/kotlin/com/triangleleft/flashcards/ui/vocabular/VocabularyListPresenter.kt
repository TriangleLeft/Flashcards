package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyModule
import com.triangleleft.flashcards.ui.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class VocabularyListPresenter @Inject
constructor(private val vocabularyModule: VocabularyModule, private val navigator: VocabularyNavigator,
            @Named(AbstractPresenter.UI_SCHEDULER) scheduler: Scheduler)
    : AbstractRxPresenter<VocabularyListView, VocabularyListViewState, VocabularyListEvent>(scheduler) {

    companion object {
        private val logger = LoggerFactory.getLogger(VocabularyListPresenter::class.java)
    }

    private val initialState: VocabularyListViewState

    init {
        initialState = VocabularyListViewState(VocabularyListViewState.Page.Progress, 0, false, false)
    }

    override fun onCreate(savedViewState: VocabularyListViewState?) {
        logger.debug("onCreate() called")

        val state = savedViewState?.copy(page = VocabularyListViewState.Page.Progress) ?: initialState

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

        val wordSelectionTransformer = ObservableTransformer<VocabularyListEvent.WordSelect, ViewAction<VocabularyListViewState>> {
            upstream ->
            upstream.doOnNext { navigator.showWord(it.word) }.map { VocabularyListViewActions.selectWord(it.position) }
        }

        val transformer = ObservableTransformer<VocabularyListEvent, ViewAction<VocabularyListViewState>> {
            events ->
            events.publish { shared ->
                Observable.merge(
                        shared.ofType(VocabularyListEvent.WordSelect::class.java).compose(wordSelectionTransformer),
                        shared.ofType(VocabularyListEvent.Refresh::class.java).compose(refreshTransformer),
                        shared.ofType(VocabularyListEvent.Load::class.java).compose(loadTransformer)
                )
            }
        }

        setup(transformer, state)
        // Start with load
        viewEvents().onNext(VocabularyListEvent.Load)
    }

    override fun getInstanceViewState(): VocabularyListViewState {
        val state = super.getInstanceViewState()
        val page = state.page
        // So, when we are saving viewstate, if current one is content, don't save list
        // but save position
        if (page is VocabularyListViewState.Page.Content) {
            return state.copy(page = VocabularyListViewState.Page.Content(emptyList()))
        } else {
            return state
        }
    }
}

