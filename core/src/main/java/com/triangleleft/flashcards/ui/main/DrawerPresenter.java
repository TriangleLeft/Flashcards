package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.login.ViewState;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
@ActivityScope
public class DrawerPresenter extends AbstractPresenter<IDrawerView, ViewState> {

    private static final Logger logger = LoggerFactory.getLogger(DrawerPresenter.class);
    private final MainPresenter mainPresenter;
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;
    private Optional<Language> currentLanguage = Optional.empty();
    private Scheduler scheduler;

    @Inject
    public DrawerPresenter(MainPresenter mainPresenter, AccountModule accountModule, SettingsModule settingsModule,
                           @Named(VIEW_EXECUTOR) Executor executor) {
        super(IDrawerView.class, executor);
        this.mainPresenter = mainPresenter;
        this.accountModule = accountModule;
        this.settingsModule = settingsModule;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() called");
        currentLanguage = accountModule.getUserData().get().getCurrentLearningLanguage();
        applyState(IDrawerView::showListProgress);
        // start with cached data, continue with fresh one
        // NOTE: we don't show progress bar while we are doing it, progress bar is shown only for language switch
        // NOTE: we don't want to notify user that we failed to update data
        // NOTE: we have to materialze/dematerialze because of this:
        // https://github.com/ReactiveX/RxJava/issues/2887
        // TODO: dispose
        settingsModule.loadUserData()
                .startWith(accountModule.getUserData().get())
                .materialize()
                //       .observeOn(scheduler)
                .<UserData>dematerialize()
                .subscribe(this::processUserData, this::processUserDataError);
    }

    private void processUserData(UserData data) {
        logger.debug("processUserData() called with: data = [{}]", data);
        Optional<Language> newCurrentLanguage = data.getCurrentLearningLanguage();
        // Notify that we need to reload list only if current language has changed
        if (!currentLanguage.equals(newCurrentLanguage)) {
            mainPresenter.onLanguageChanged(newCurrentLanguage.get());
        }
        currentLanguage = newCurrentLanguage;
        applyState(view -> view.showUserData(data.getUsername(), data.getAvatar(), data.getSortedLanguages()));
    }

    private void processUserDataError(Throwable throwable) {
        logger.debug("processUserDataError() called with: throwable = [{}]", throwable);
        getView().notifyUserDataUpdateError();
        // There is no need to show local userdata, as we should've done so with .startWith
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onLanguageSelected(final Language language) {
        logger.debug("onLanguageSelected() called with: language = [{}]", language);
        // Ignore clicks on the same language
        if (currentLanguage.isPresent() && currentLanguage.get().equals(language)) {
            return;
        }
        // Show progress bar
        applyState(IDrawerView::showListProgress);
        // Request to switch language
        settingsModule.switchLanguage(language)
                .enqueue(data -> processLanguageSwitch(language), this::processLanguageSwitchError);
    }

    private void processLanguageSwitch(Language currentLanguage) {
        logger.debug("processLanguageSwitch() called with: currentLanguage = [{}]", currentLanguage);
        // We have successfully switch language
        // Update our local userdata
        UserData data = accountModule.getUserData().get();
        // Mark language as "current learning"
        List<Language> languages = Stream.of(data.getLanguages())
                .map(language -> language.withCurrentLearning(language.getId().equals(currentLanguage.getId())))
                .collect(toList());
        // Override local userdata
        UserData newData = data.withLanguages(languages).withLearningLanguageId(currentLanguage.getId());
        accountModule.setUserData(newData);
        processUserData(newData);
    }

    private void processLanguageSwitchError(Throwable throwable) {
        logger.debug("processLanguageSwitchError() called with: throwable = [{}]", throwable);
        getView().notifyLanguageSwitchError();
        // Show local userdata
        processUserData(accountModule.getUserData().get());
    }
}
