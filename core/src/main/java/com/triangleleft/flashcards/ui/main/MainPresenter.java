package com.triangleleft.flashcards.ui.main;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.login.ViewState;
import com.triangleleft.flashcards.ui.vocabular.VocabularyNavigator;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

@FunctionsAreNonnullByDefault
@ActivityScope
public class MainPresenter extends AbstractPresenter<IMainView, ViewState> implements VocabularyNavigator {

    private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);
    private final AccountModule accountModule;
    private Page currentPage;
    private String title;
    private Optional<VocabularyWord> currentWord = Optional.empty();
    private boolean isSettingsDialogShown;

    @Inject
    public MainPresenter(AccountModule accountModule, @Named(VIEW_EXECUTOR) Executor executor) {
        super(IMainView.class, executor);
        this.accountModule = accountModule;
    }

    @Override
    public void onCreate() {
        Optional<UserData> userData = accountModule.getUserData();
        Utils.checkState(userData.isPresent(), "Showing main screen without userdata present");
        // It's possible that we are using account that doesn't have any languages
        userData.get().getCurrentLearningLanguage().ifPresent(language -> title = language.getName());
        currentPage = Page.LIST;
    }

    @Override
    public void onBind(IMainView view) {
        super.onBind(view);
        switch (currentPage) {
            case LIST:
                view.showList();
                break;
            case WORD:
                view.showWord(currentWord);
                break;
            default:
                throw new IllegalStateException("Unknown state " + currentPage);
        }
        view.setTitle(title);
        if (isSettingsDialogShown) {
            view.showFlashcardsDialog();
        }
    }

    @Override
    public void showWord(@NonNull Optional<VocabularyWord> word) {
        logger.debug("showWord() called with: word = [{}]", word);
        currentPage = Page.WORD;
        currentWord = word;
        getView().showWord(word);
    }

    public void onBackPressed() {
        if (currentPage == Page.LIST) {
            // list is considered top-level screen, so pressing back here should close app
            getView().finish();
        } else {
            // Go to top-level screen
            currentPage = Page.LIST;
            getView().showList();
        }
    }

    public void onLanguageChanged(Language currentLanguage) {
        logger.debug("onLanguageChanged() called with: currentLanguage = [{}]", currentLanguage);
        title = currentLanguage.getName();
        getView().setTitle(title);
        getView().reloadList();
    }

    public void setSettingsDialogShown(boolean isShown) {
        isSettingsDialogShown = isShown;
    }

    private enum Page {
        LIST,
        WORD
    }
}
