package com.triangleleft.flashcards.mvp.main;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.vocabular.VocabularyNavigator;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;

@FunctionsAreNonnullByDefault
@ActivityScope
public class MainPresenter extends AbstractPresenter<IMainView> implements VocabularyNavigator {

    private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;
    private final Scheduler scheduler;
    private final Comparator<Language> languageComparator =
            (l1, l2) -> Boolean.valueOf(l2.isCurrentLearning()).compareTo(l1.isCurrentLearning());

    private Language currentLanguage;
    private IMainView.Page currentPage;

    @Inject
    public MainPresenter(AccountModule accountModule, SettingsModule settingsModule, Scheduler scheduler) {
        super(IMainView.class);
        this.accountModule = accountModule;
        this.settingsModule = settingsModule;
        this.scheduler = scheduler;
    }

    @Override
    public void onCreate() {
        // Update user data here?
        applyState(view -> {
            currentPage = IMainView.Page.LIST;
            view.showList();
        });
    }

    @Override
    public void onBind(IMainView view) {
        super.onBind(view);
        Optional<UserData> userData = accountModule.getUserData();
        // It's possible that local data was wiped
        if (userData.isPresent()) {
            showUserData(userData.get());
        } else {
            getView().navigateToLogin();
        }
    }

    @Override
    public void showWord(@NonNull Optional<VocabularyWord> word) {
        logger.debug("showWord() called with: word = [{}]", word);
        applyState(view -> {
            currentPage = IMainView.Page.WORD;
            view.showWord(word);
        });
    }

    public void onBackPressed() {
        if (currentPage == IMainView.Page.LIST) {
            // list is considered top-level screen, so pressing back here should close app
            getView().finish();
        } else {
            // Go to top-level screen
            applyState(view -> {
                currentPage = IMainView.Page.LIST;
                view.showList();
            });
        }
    }

    public void onLanguageSelected(Language language) {
        if (language.equals(currentLanguage)) {
            return;
        }
        applyState(IMainView::showDrawerProgress);
        settingsModule.switchLanguage(language)
                .switchMap(nothing -> settingsModule.loadUserData())
                .observeOn(scheduler)
                .subscribe(data -> {
                            showUserData(data);
                            getView().reloadList();
                        },
                        error -> {
                            logger.error("switchLanguage()", error);
                            applyState(IMainView::showDrawerError);
                        }
                );
    }

    private void showUserData(UserData userData) {
        // We assume that is only one language that we are currently learning
        // Sort it, so it is always top of the list
        List<Language> languages = Stream.of(userData.getLanguages())
                .filter(Language::isLearning)
                .sorted(languageComparator)
                .collect(Collectors.toList());

        currentLanguage = languages.size() > 0 ? languages.get(0) : null;

        getView().showUserData(userData.getUsername(), userData.getAvatar(), languages);
    }

    public void onLogoutClick() {
        accountModule.setRememberUser(false);
        getView().navigateToLogin();
    }
}
