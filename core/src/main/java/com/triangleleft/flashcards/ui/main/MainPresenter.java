package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.di.scope.ActivityScope;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.settings.UserData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.vocabular.VocabularyNavigator;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
@ActivityScope
public class MainPresenter extends AbstractPresenter<IMainView> implements VocabularyNavigator {

    private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);
    private final AccountModule accountModule;
    private Page currentPage;
    private String title;
    private Optional<VocabularyWord> currentWord = Optional.empty();

    @Inject
    public MainPresenter(AccountModule accountModule) {
        super(IMainView.class);
        this.accountModule = accountModule;
    }

    @Override
    public void onCreate() {
        UserData userData = accountModule.getUserData().get();
        userData.getCurrentLearningLanguage().ifPresent(language -> title = language.getName());
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
        title = currentLanguage.getName();
        getView().setTitle(title);
        getView().reloadList();
    }

    private enum Page {
        LIST,
        WORD
    }
}
