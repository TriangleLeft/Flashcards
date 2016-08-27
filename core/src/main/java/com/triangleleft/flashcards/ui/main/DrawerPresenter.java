package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscription;

@FunctionsAreNonnullByDefault
@ActivityScope
public class DrawerPresenter extends AbstractPresenter<IDrawerView> {

    private static final Logger logger = LoggerFactory.getLogger(DrawerPresenter.class);
    private final MainPresenter mainPresenter;
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;
    private final Scheduler scheduler;
    private Optional<Language> currentLanguage = Optional.empty();
    private Subscription subscription;

    @Inject
    public DrawerPresenter(MainPresenter mainPresenter, AccountModule accountModule, SettingsModule settingsModule,
                           Scheduler scheduler) {
        super(IDrawerView.class);
        this.mainPresenter = mainPresenter;
        this.accountModule = accountModule;
        this.settingsModule = settingsModule;
        this.scheduler = scheduler;
    }

    @Override
    public void onCreate() {
        // start with cached data, continue with fresh one
        // NOTE: we don't show progress bar while we are doing it
        subscription = settingsModule.loadUserData()
                .startWith(accountModule.getUserData().get())
                .observeOn(scheduler)
                .subscribe(this::processData, this::processError);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    public void onLanguageSelected(final Language language) {
        logger.debug("onLanguageSelected() called with: language = [{}]", language);
        if (currentLanguage.isPresent() && currentLanguage.get().equals(language)) {
            return;
        }
        // Cancel update if it's hasn't completed yet
        subscription.unsubscribe();
        applyState(IDrawerView::showDrawerProgress);
        subscription = settingsModule.switchLanguage(language)
                .switchMap(nothing -> settingsModule.loadUserData())
                .observeOn(scheduler)
                .subscribe(this::processData, this::processError);
    }

    private void processData(UserData data) {
        logger.debug("processData() called with: data = [{}]", data);
        Optional<Language> newCurrentLanguage = data.getCurrentLearningLanguage();
        // Notify that we need to reload list only if current language has changed
        if (!currentLanguage.equals(newCurrentLanguage)) {
            mainPresenter.onLanguageChanged(newCurrentLanguage.get());
        }
        currentLanguage = newCurrentLanguage;
        applyState(view -> view.showUserData(data.getUsername(), data.getAvatar(), data.getSortedLanguages()));
    }

    private void processError(Throwable throwable) {
        logger.debug("processError() called with: throwable = [{}]", throwable);
        getView().notifyLanguageSwitchError();
    }
}
