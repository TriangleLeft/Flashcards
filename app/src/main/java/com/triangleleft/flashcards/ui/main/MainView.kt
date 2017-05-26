package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface MainView : IView<MainView.State, ViewEvent> {

    fun backPresses(): Observable<Any>

    fun wordClicks(): Observable<VocabularyWord>

    data class State(val title: String, val flashcardsDialogIsShown: Boolean, val page: Page) : ViewState {

        sealed class Page {
            object List : Page()
            object Exit : Page()
            data class Word(val word: VocabularyWord?) : Page()
        }

    }

}
