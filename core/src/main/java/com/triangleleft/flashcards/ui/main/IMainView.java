package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.settings.Language;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.view.IView;

import java.util.List;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(Optional<VocabularyWord> word);

    void showList();

    void finish();

    void reloadList();

    void showDrawerProgress();

    void showUserData(String username, String avatar, List<Language> languages);

    void showDrawerError();

    enum Page {
        LIST,
        WORD
    }
}
