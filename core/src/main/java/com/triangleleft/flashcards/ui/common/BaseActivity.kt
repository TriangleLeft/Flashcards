package com.triangleleft.flashcards.ui.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.triangleleft.flashcards.di.IComponent
import com.triangleleft.flashcards.ui.BaseApplication
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.presenter.IPresenter
import com.triangleleft.flashcards.ui.common.view.IView
import org.slf4j.LoggerFactory
import javax.inject.Inject

abstract class BaseActivity<Component : IComponent, View : IView<*, *>, VS : ViewState, Presenter : IPresenter<View, VS>> : AppCompatActivity() {

    lateinit var component: Component

    @Inject
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState)

        val restoredComponent = baseApplication.componentManager.restoreComponent<Component>(javaClass)
        this.component = restoredComponent ?: buildComponent()

        inject()
        // If we didn't had restored component, it's a new one, run presenter's onCreate
        // So... if it's first launch after process death, we have no presenter, but we could have saved instance state
        if (restoredComponent == null) {
            val viewState: VS? = null
            // TODO: fix state serialization
            presenter.onCreate(viewState)
        }

        // We want components/presenters to be created before fragments are instantiated
        super.onCreate(savedInstanceState)
    }

    protected abstract fun inject()

    override fun onStart() {
        logger.debug("onStart() called")
        super.onStart()
        presenter.onBind(mvpView)
    }

    override fun onStop() {
        logger.debug("onStop() called")
        super.onStop()
        presenter.onUnbind()
    }

    override fun onDestroy() {
        logger.debug("onDestroy() called")
        super.onDestroy()
        // Activity instance should be GCed
        baseApplication.refWatcher.watch(this)
        if (!isChangingConfigurations) {
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
        get() = application as BaseApplication


    protected abstract fun buildComponent(): Component

    protected abstract val mvpView: View

    companion object {
        private val logger = LoggerFactory.getLogger(BaseActivity::class.java.simpleName)
    }
}
