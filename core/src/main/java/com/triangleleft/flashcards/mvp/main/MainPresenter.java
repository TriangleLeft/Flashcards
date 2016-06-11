package com.triangleleft.flashcards.mvp.main;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

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
    private final ISettingsModule settingsModule;
    private final Scheduler scheduler;
    private final Comparator<ILanguage> languageComparator =
            (l1, l2) -> Boolean.valueOf(l2.isCurrentLearning()).compareTo(l1.isCurrentLearning());
    private IMainView.Page currentPage = IMainView.Page.LIST;
    private IVocabularWord selectedWord;
    private IUserData userData;


    @Inject
    public MainPresenter(ISettingsModule settingsModule, Scheduler scheduler) {
        super(IMainView.class);
        this.settingsModule = settingsModule;
        this.scheduler = scheduler;
    }

    @Override
    public void onCreate() {
        loadUserData();
    }

    @Override
    public void onBind(IMainView view) {
        super.onBind(view);
        showViewPage(currentPage);
        if (userData != null) {
            showUserData(userData);
        }
    }

    @Override
    public void onWordSelected(@NonNull IVocabularWord word) {
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

    public void onLanguageSelected(ILanguage language) {
        getView().showDrawerProgress();
        settingsModule.switchLanguage(language)
                .observeOn(scheduler)
                .subscribe(nothing -> loadUserData());
    }

    private void loadUserData() {
        settingsModule.getUserData()
                .observeOn(scheduler)
                .subscribe(userData -> {
                            this.userData = userData;
                            showUserData(userData);
                        },
                        error -> {
                        }
                );
    }

    private void showUserData(IUserData userData) {
        // We assume that is only one language that we are currently learning
        // Sort it, so it is always top of the list
        List<ILanguage> languages = Stream.of(userData.getLanguages())
                .filter(ILanguage::isLearning)
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
