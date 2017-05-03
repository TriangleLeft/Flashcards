package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.common.view.IView
import io.reactivex.Observable

interface IDrawerView : IView {

    fun render(state: DrawerViewState)

    fun languageClicks(): Observable<Language>

    fun errorShowCompletes(): Observable<Any>

    enum class Page {
        CONTENT,
        PROGRESS
    }
}
