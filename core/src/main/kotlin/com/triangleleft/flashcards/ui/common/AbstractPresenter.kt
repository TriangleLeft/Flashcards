package com.triangleleft.flashcards.ui.common.presenter

import android.support.annotation.CallSuper
import android.support.annotation.UiThread
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.view.IView
import org.slf4j.LoggerFactory

abstract class AbstractPresenter<View : IView<VS, VE>, VS : ViewState, VE : ViewEvent> : IPresenter<View, VS> {

    override var view: View? = null

    override fun onCreate(savedViewState: VS?) {

    }

    override fun onDestroy() {

    }

    override fun onViewCreated() {

    }

    @UiThread
    @CallSuper
    override fun onBind(view: View) {
        logger.debug("onRebind() called with: view = [{}]", view)
        this.view = view
    }

    @UiThread
    @CallSuper
    override fun onUnbind() {
        logger.debug("onUnbind() called")
        view = null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AbstractPresenter::class.java)
    }

}
