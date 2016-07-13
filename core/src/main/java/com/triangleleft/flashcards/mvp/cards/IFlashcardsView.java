package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

public interface IFlashcardsView extends IView {

    void showTestData(List<FlashcardWord> wordList);

    void showProgress();

    void showErrorNoContent();

    void showResultsNoErrors();

    void showResultErrors(List<FlashcardWord> wordList);
}
