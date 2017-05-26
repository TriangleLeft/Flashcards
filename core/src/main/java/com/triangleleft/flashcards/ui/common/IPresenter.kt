package com.triangleleft.flashcards.ui.common.presenter

import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView

interface IPresenter<View : IView<*, *>, VS : ViewState> {

    fun onCreate(savedViewState: VS?)

    fun onViewCreated()

    fun onBind(view: View)

    fun onUnbind()

    var view: View?

    fun onDestroy()
}
