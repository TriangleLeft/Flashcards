package com.triangleleft.flashcards.ui.common.view

import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import io.reactivex.Observable

interface IView<in VS : ViewState, VE : ViewEvent> {
    fun render(viewState: VS)

    fun events(): Observable<VE>
}
