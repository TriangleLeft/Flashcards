package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.di.scope.FragmentScope
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewAction
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope
class VocabularyWordPresenter @Inject
constructor() : AbstractPresenter<IVocabularyWordView, VocabularyWordViewState, ViewEvent>() {

    private val words = PublishSubject.create<VocabularyWord?>()
    private val viewStates = BehaviorSubject.create<VocabularyWordViewState>()
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

    override fun onRebind(view: IVocabularyWordView) {
        super.onRebind(view)

        disposable = CompositeDisposable()
        disposable.addAll(
                viewStates.subscribe { view.render(it) }
        )
    }

    override fun onUnbind() {
        super.onUnbind()
        disposable.dispose()
    }

    fun showWord(word: VocabularyWord?) {
        words.onNext(word)
    }
}
