package com.triangleleft.flashcards.mvp.main;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;

@ActivityScope
public class MainPresenter extends AbstractPresenter<IMainView> implements IVocabularNavigator {

    private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);
    private final SettingsModule settingsModule;
    private final Scheduler scheduler;
    private final Comparator<Language> languageComparator =
            (l1, l2) -> Boolean.valueOf(l2.isCurrentLearning()).compareTo(l1.isCurrentLearning());
    private IMainView.Page currentPage = IMainView.Page.LIST;
    private VocabularWord selectedWord;

    @Inject
    public MainPresenter(SettingsModule settingsModule, Scheduler scheduler) {
        super(IMainView.class);
        this.settingsModule = settingsModule;
        this.scheduler = scheduler;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onBind(IMainView view) {
        super.onBind(view);
        showViewPage(currentPage);
        showUserData(settingsModule.getCurrentUserData());
    }

    @Override
    public void onWordSelected(@NonNull VocabularWord word) {
        logger.debug("onWordSelected() called with: word = [{}]", word);
        selectedWord = word;
        showViewPage(IMainView.Page.WORD);
    }

    public void onBackPressed() {
        if (currentPage == IMainView.Page.LIST) {
            // list is considered top-level screen, so pressing back here should close app
            getView().finish();
        } else {
            // Go to top-level screen
            showViewPage(IMainView.Page.LIST);
        }
    }

    public void onLanguageSelected(Language language) {
        getView().showDrawerProgress();
        settingsModule.switchLanguage(language)
                .observeOn(scheduler)
                .subscribe(success -> loadUserData(),
                        error -> {

                        });
    }

    private void loadUserData() {
        settingsModule.getUserData()
                .observeOn(scheduler)
                .subscribe(this::showUserData,
                        error -> {
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


        getView().showUserData(userData.getUsername(), userData.getAvatar(), languages);
    }

    private void showViewPage(IMainView.Page page) {
        currentPage = page;
        switch (currentPage) {
            case LIST:
                getView().showList();
                break;
            case WORD:
                getView().showWord(selectedWord);
                break;
            default:
                throw new RuntimeException("Unknown page: " + currentPage);
        }
    }
}
