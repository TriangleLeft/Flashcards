package com.triangleleft.flashcards.ui.vocabular;

import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.view.IView;

public interface IVocabularyWordView extends IView {
    void showWord(VocabularyWord word);

    void showEmpty();
}
