package com.triangleleft.flashcards.mvp.vocabular;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

public interface IVocabularyWordView extends IView {
    void showWord(VocabularyWord word);

    void showEmpty();
}
