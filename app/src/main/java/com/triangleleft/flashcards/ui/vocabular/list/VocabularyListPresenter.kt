package com.triangleleft.flashcards.ui.vocabular.list

import com.triangleleft.flashcards.di.main.MainPageModule
import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyModule
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class VocabularyListPresenter
@Inject
constructor(
        private val vocabularyModule: VocabularyModule,
        @Named(MainPageModule.WORD_SELECTIONS)
        private val wordSelections: Observer<VocabularyWord>,
        @Named(UI_SCHEDULER) scheduler: Scheduler)
    : AbstractRxPresenter<VocabularyListView, VocabularyListView.State, VocabularyListView.Event>(scheduler) {

    companion object {
        private val logger = LoggerFactory.getLogger(VocabularyListPresenter::class.java)
    }

    override fun onCreate(savedViewState: VocabularyListView.State?) {

        val initialState: VocabularyListView.State = VocabularyListView.State(VocabularyListView.State.Page.Progress,
                0, false, false)
        val state = savedViewState?.copy(page = VocabularyListView.State.Page.Progress) ?: initialState

        val transformer = ObservableTransformer<VocabularyListView.Event, ViewAction<VocabularyListView.State>> {
            events ->
            events.flatMap {
                when (it) {
                    is VocabularyListView.Event.WordSelect -> {
                        wordSelections.onNext(it.word)
                        just(VocabularyListViewActions.selectWord(it.position))
                    }
                    VocabularyListView.Event.Refresh -> refreshWords()
                    VocabularyListView.Event.Load -> loadWords()
                }
            }
        }

        setup(viewEvents().compose(transformer), state)
        // Start with load
        viewEvents().onNext(VocabularyListView.Event.Load)
    }

    fun refreshWords(): Observable<ViewAction<VocabularyListView.State>> {
        return vocabularyModule.refreshVocabularyWords()
                .map { VocabularyListViewActions.showList(it) }
                .startWith { VocabularyListViewActions.refreshProgress() }
                .onErrorReturn { VocabularyListViewActions.refreshError() }
    }

    fun loadWords(): Observable<ViewAction<VocabularyListView.State>> {
        return vocabularyModule.loadVocabularyWords()
                .map { VocabularyListViewActions.showList(it) }
                .startWith(VocabularyListViewActions.progress())
                .onErrorReturn { VocabularyListViewActions.error() }
    }

//    override fun getInstanceViewState(): VocabularyListView.State {
//        val state = super.getInstanceViewState()
//        val page = state.page
//        // So, when we are saving viewstate, if current one is content, don't save list
//        // but save position
//        if (page is VocabularyListView.State.Page.Content) {
//            return state.copy(page = VocabularyListView.State.Page.Content(emptyList()))
//        } else {
//            return state
//        }
//    }

    private object VocabularyListViewActions {

        fun showList(list: List<VocabularyWord>): ViewAction<VocabularyListView.State> {
            return ViewAction { state ->
                // So, we also reset refresh status here
                state.copy(page = VocabularyListView.State.Page.Content(list), hasRefreshError = false,
                        isRefreshing = false)
            }
        }

        fun progress(): ViewAction<VocabularyListView.State> {
            return ViewAction { state -> state.copy(page = VocabularyListView.State.Page.Progress) }
        }

        fun error(): ViewAction<VocabularyListView.State> {
            return ViewAction { state -> state.copy(page = VocabularyListView.State.Page.Error) }
        }

        fun selectWord(position: Int): ViewAction<VocabularyListView.State> {
            return ViewAction { state -> state.copy(selectedPosition = position) }
        }

        fun refreshError(): ViewAction<VocabularyListView.State> {
            return ViewAction { state -> state.copy(hasRefreshError = true) }
        }

        fun refreshProgress(): ViewAction<VocabularyListView.State> {
            return ViewAction { state -> state.copy(isRefreshing = true) }
        }
    }
}

