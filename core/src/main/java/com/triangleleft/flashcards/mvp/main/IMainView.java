package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import java.util.List;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(IVocabularWord word);

    void showList();

    void finish();

    void showDrawerProgress();

    void showUserData(String username, String avatar, List<ILanguage> languages);

    enum Page {
        LIST,
        WORD
    }
}
