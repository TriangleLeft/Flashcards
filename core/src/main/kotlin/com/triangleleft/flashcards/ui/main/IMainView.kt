package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface IMainView : IView {

    fun render(it: MainViewState)

    fun backPresses(): Observable<Any>

    fun wordClicks(): Observable<VocabularyWord>

}
