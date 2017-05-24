package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface IMainView : IView<MainViewState, ViewEvent> {

    fun backPresses(): Observable<Any>

    fun wordClicks(): Observable<VocabularyWord>

}
