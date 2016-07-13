package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.cards.FlashcardWord;

import java.util.List;

public interface IFlashcardsView extends IView {

    void showWords(List<FlashcardWord> wordList);

    void showProgress();

    void showError();

    void showResultsNoErrors();

    void showResultErrors(List<FlashcardWord> wordList);
}
