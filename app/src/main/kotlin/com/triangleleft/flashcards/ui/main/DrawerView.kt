package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface DrawerView : IView<DrawerViewState, ViewEvent> {

    fun languageClicks(): Observable<Language>

    fun errorShowCompletes(): Observable<Any>

    enum class Page {
        CONTENT,
        PROGRESS
    }
}
