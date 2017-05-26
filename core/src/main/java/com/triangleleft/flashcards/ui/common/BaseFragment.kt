package com.triangleleft.flashcards.ui.common

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import com.triangleleft.flashcards.di.IComponent
import com.triangleleft.flashcards.ui.BaseApplication
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.presenter.IPresenter
import com.triangleleft.flashcards.ui.common.view.IView
import org.slf4j.LoggerFactory
import javax.inject.Inject

abstract class BaseFragment<Component : IComponent, View : IView<*, *>, VS : ViewState, Presenter : IPresenter<View, VS>> : Fragment() {
    @Inject
    lateinit var presenter: Presenter
    lateinit var component: Component

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState)

        val restoredComponent = baseApplication.componentManager.restoreComponent<Component>(javaClass)
        this.component = restoredComponent ?: buildComponent()

        inject()
        // If we didn't had restored component, it's a new one, run presenter's onCreate
        // So... if it's first launch after process death, we have no presenter, but we could have saved instance state
        if (restoredComponent == null) {
            val viewState: VS? = null
            // TODO: support this
            presenter.onCreate(viewState)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    protected abstract fun inject()

    @CallSuper
    override fun onResume() {
        logger.debug("onResume() called")
        super.onResume()
        presenter.onBind(mvpView)
    }

    @CallSuper
    override fun onPause() {
        logger.debug("onPause() called")
        super.onPause()
        presenter.onUnbind()
    }

    @CallSuper
    override fun onDestroy() {
        logger.debug("onDestroy() called")
        super.onDestroy()
        baseApplication.refWatcher.watch(this)
        if (isRemoving || !activity.isChangingConfigurations) {
            // We are leaving this screen, notify presenter
            presenter.onDestroy()
            // Component and presenter should be GCed
            baseApplication.refWatcher.watch(component)
            baseApplication.refWatcher.watch(presenter)
        } else {
            baseApplication.componentManager.saveComponent(javaClass, component)
        }
    }

    protected val baseApplication: BaseApplication
        get() = activity.application as BaseApplication

    protected abstract fun buildComponent(): Component

    protected abstract val mvpView: View

    companion object {

        private val logger = LoggerFactory.getLogger(BaseFragment::class.java)
    }
}
