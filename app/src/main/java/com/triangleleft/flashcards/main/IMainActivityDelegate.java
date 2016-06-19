package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.service.vocabular.VocabularWord;

public interface IMainActivityDelegate {
    void showList();

    void showWord(VocabularWord word);

    boolean isDrawerOpen();

    void closeDrawer();

    void reloadList();
}
