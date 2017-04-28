package com.triangleleft.flashcards.ui.vocabular

import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface IVocabularyListView : IView {

    fun render(state: VocabularyListViewState)

    fun refreshes(): Observable<Unit>

    fun wordSelections(): Observable<VocabularyWordSelectEvent>

    // Can't we reuse refereshes here?
    fun loadListRetires(): Observable<Unit>

}
