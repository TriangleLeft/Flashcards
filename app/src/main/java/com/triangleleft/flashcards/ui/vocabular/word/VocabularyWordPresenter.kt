package com.triangleleft.flashcards.ui.vocabular.word

import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.ViewAction
import com.triangleleft.flashcards.ui.common.presenter.AbstractRxPresenter
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

@FragmentScope class VocabularyWordPresenter @Inject constructor(
        @Named(UI_SCHEDULER) scheduler: Scheduler
) : AbstractRxPresenter<VocabularyWordView, VocabularyWordView.State, ViewEvent>(scheduler) {

    private val words = PublishSubject.create<VocabularyWord?>()

    override fun onCreate(savedViewState: VocabularyWordView.State?) {
        super.onCreate(savedViewState)

        val state = savedViewState ?: VocabularyWordView.State(null)

        val observable = words.map { word -> ViewAction<VocabularyWordView.State> { state -> state.copy(word = word) } }

        setup(observable, state)
    }

    fun showWord(word: VocabularyWord?) {
        words.onNext(word)
    }
}
