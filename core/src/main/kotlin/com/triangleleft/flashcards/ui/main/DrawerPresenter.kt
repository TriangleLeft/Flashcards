package com.triangleleft.flashcards.ui.main

import com.triangleleft.flashcards.di.scope.ActivityScope
import com.triangleleft.flashcards.service.account.AccountModule
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.service.settings.SettingsModule
import com.triangleleft.flashcards.service.settings.UserData
import com.triangleleft.flashcards.ui.ViewState
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

@FunctionsAreNonnullByDefault
@ActivityScope
class DrawerPresenter @Inject
constructor(private val mainPresenter: MainPresenter, private val accountModule: AccountModule, private val settingsModule: SettingsModule,
            @Named(AbstractPresenter.VIEW_EXECUTOR) executor: Executor) : AbstractPresenter<IDrawerView, ViewState>(IDrawerView::class.java, executor) {

    var currentLanguage: Language? = null
    private val scheduler: Scheduler? = null

    override fun onCreate() {
        logger.debug("onCreate() called")
        currentLanguage = accountModule.userData.get().currentLearningLanguage
        applyState { it.showListProgress() }
        // start with cached data, continue with fresh one
        // NOTE: we don't show progress bar while we are doing it, progress bar is shown only for language switch
        // NOTE: we don't want to notify user that we failed to update data
        // NOTE: we have to materialze/dematerialze because of this:
        // https://github.com/ReactiveX/RxJava/issues/2887
        // TODO: dispose
        settingsModule.loadUserData()
                .startWith(accountModule.userData.get())
                .materialize()
                //       .observeOn(scheduler)
                .dematerialize<UserData>()
                .subscribe({ this.processUserData(it) }, { this.processUserDataError(it) })
    }

    private fun processUserData(data: UserData) {
        logger.debug("processUserData() called with: data = [{}]", data)
        val newCurrentLanguage = data.currentLearningLanguage
        // Notify that we need to reload list only if current language has changed
        if (currentLanguage != newCurrentLanguage) {
            mainPresenter.onLanguageChanged(newCurrentLanguage!!)
        }
        currentLanguage = newCurrentLanguage
        applyState { view -> view.showUserData(data.username, data.avatar, data.sortedLanguages) }
    }

    private fun processUserDataError(throwable: Throwable) {
        logger.debug("processUserDataError() called with: throwable = [{}]", throwable)
        view.notifyUserDataUpdateError()
        // There is no need to show local userdata, as we should've done so with .startWith
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onLanguageSelected(language: Language) {
        logger.debug("onLanguageSelected() called with: language = [{}]", language)
        // Ignore clicks on the same language
        if (currentLanguage != null && currentLanguage == language) {
            return
        }
        // Show progress bar
        applyState { it.showListProgress() }
        // Request to switch language
        settingsModule.switchLanguage(language)
                .subscribe({ processLanguageSwitch(language) }, { this.processLanguageSwitchError(it) })
    }

    private fun processLanguageSwitch(currentLanguage: Language) {
        logger.debug("processLanguageSwitch() called with: currentLanguage = [{}]", currentLanguage)
        // We have successfully switch language
        // Update our local userdata
        val data = accountModule.userData.get()
        // Mark language as "current learning"
        val languages = data.languages
                .map { language -> language.withCurrentLearning(language.id == currentLanguage.id) }
        // Override local userdata
        val newData = data.copy(languages = languages, learningLanguageId = currentLanguage.id)
        accountModule.setUserData(newData)
        processUserData(newData)
    }

    private fun processLanguageSwitchError(throwable: Throwable) {
        logger.debug("processLanguageSwitchError() called with: throwable = [{}]", throwable)
        view.notifyLanguageSwitchError()
        // Show local userdata
        processUserData(accountModule.userData.get())
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DrawerPresenter::class.java)
    }
}
