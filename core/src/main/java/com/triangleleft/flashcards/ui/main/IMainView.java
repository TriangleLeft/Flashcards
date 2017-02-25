package com.triangleleft.flashcards.ui.main;

import com.annimon.stream.Optional;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.view.IView;

public interface IMainView extends IView {

    void setTitle(String title);

    void showWord(Optional<VocabularyWord> word);

    void showList();

    void finish();

    void reloadList();

}
