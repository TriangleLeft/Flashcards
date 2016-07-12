package com.triangleleft.flashcards.main;

import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

public interface IMainActivityDelegate {
    void showList();

    void showWord(VocabularyWord word);

    boolean isDrawerOpen();

    void closeDrawer();

    void reloadList();
}
