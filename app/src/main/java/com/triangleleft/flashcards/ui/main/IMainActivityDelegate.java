package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

public interface IMainActivityDelegate {
    void showList();

    void showWord(Optional<VocabularyWord> word);

    boolean isDrawerOpen();

    void closeDrawer();

    void reloadList();
}
