package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import java.util.List;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(VocabularyWord word);

    void showList();

    void finish();

    void showDrawerProgress();

    void showUserData(String username, String avatar, List<Language> languages);

    void navigateToLogin();

    enum Page {
        LIST,
        WORD
    }
}
