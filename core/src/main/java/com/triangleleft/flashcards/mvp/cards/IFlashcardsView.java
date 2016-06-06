package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.cards.IFlashcardTestData;

public interface IFlashcardsView extends IView {

    void showTestData(IFlashcardTestData data);

    void showProgress();

    void showErrorNoContent();

    void showResults();
}
