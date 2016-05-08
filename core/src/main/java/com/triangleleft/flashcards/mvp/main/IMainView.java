package com.triangleleft.flashcards.mvp.main;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(IVocabularWord word);

    void showList();

    void finish();

    enum Page {
        LIST,
        WORD
    }
}
