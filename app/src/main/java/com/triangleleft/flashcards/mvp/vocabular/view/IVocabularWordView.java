package com.triangleleft.flashcards.mvp.vocabular.view;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

public interface IVocabularWordView extends IView {
    void showWord(IVocabularWord selectedWord);
}
