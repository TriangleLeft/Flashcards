package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class VocabularyWordPresenter @Inject constructor(
        @Named(AbstractRxPresenter.UI_SCHEDULER) scheduler: Scheduler
) : AbstractRxPresenter<VocabularyWordView, VocabularyWordViewState, ViewEvent>(scheduler) {

    private val words = PublishSubject.create<VocabularyWord?>()
    lateinit var disposable: CompositeDisposable

    override fun onCreate(savedViewState: VocabularyWordViewState?) {
        super.onCreate(savedViewState)

        val state = savedViewState ?: VocabularyWordViewState(null)

        words.map { word -> ViewAction<VocabularyWordViewState> { state -> state.copy(word = word) } }
                .scan(state) { state, action -> action.reduce(state) }
                .distinctUntilChanged()
                .subscribe {
                    viewStates.onNext(it)
                }
    }

    fun showWord(word: VocabularyWord?) {
        words.onNext(word)
    }
}
