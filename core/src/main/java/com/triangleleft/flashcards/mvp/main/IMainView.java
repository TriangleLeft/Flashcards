package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.settings.ILanguage;
import com.triangleleft.flashcards.service.settings.IUserData;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(IVocabularWord word);

    void showList();

    void finish();

    void showUserData(IUserData userData);

    void setCurrentLanguage(ILanguage language);

    enum Page {
        LIST,
        WORD
    }
}
