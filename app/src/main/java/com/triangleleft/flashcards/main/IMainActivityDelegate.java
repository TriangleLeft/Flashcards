package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

public interface IMainActivityDelegate {
    void showList();

    void showWord(IVocabularWord word);

    boolean isDrawerOpen();

    void closeDrawer();
}
