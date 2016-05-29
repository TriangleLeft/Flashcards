package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.di.scope.ActivityScope;
import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularNavigator;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.ISettingsModule;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Scheduler;

@ActivityScope
public class MainPresenter extends AbstractPresenter<IMainView> implements IVocabularNavigator {

    private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);
    private final ISettingsModule settingsModule;
    private final Scheduler scheduler;
    private IMainView.Page currentPage = IMainView.Page.LIST;
    private IVocabularWord selectedWord;


    @Inject
    public MainPresenter(ISettingsModule settingsModule, Scheduler scheduler) {
        super(IMainView.class);
        this.settingsModule = settingsModule;
        this.scheduler = scheduler;
    }

    @Override
    public void onCreate() {
        settingsModule.getUserData()
                .observeOn(scheduler)
                .subscribe(
                        userData -> {
                            getView().showUserData(userData);
                        },
                        error -> {
                        }
                );
    }

    @Override
    public void onBind(IMainView view) {
        super.onBind(view);
        setViewPage(currentPage);
    }

    public void onBackPressed() {
        if (currentPage == IMainView.Page.LIST) {
            // list is considered top-level screen, so pressing back here should close app
            getView().finish();
        } else {
            // Go to top-level screen
            setViewPage(IMainView.Page.LIST);
        }
    }

    @Override
    public void onWordSelected(@NonNull IVocabularWord word) {
        logger.debug("onWordSelected() called with: word = [{}]", word);
        selectedWord = word;
        setViewPage(IMainView.Page.WORD);
    }

    private void setViewPage(IMainView.Page page) {
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

    public void onLanguageSelected(ILanguage language) {
        settingsModule.switchLanguage(language)
                .observeOn(scheduler)
                .subscribe(nothing -> getView().setCurrentLanguage(language));
    }
}
