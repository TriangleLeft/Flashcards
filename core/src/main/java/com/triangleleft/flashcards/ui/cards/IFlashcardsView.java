package com.triangleleft.flashcards.ui.cards;

import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.ui.common.view.IView;

import java.util.List;

public interface IFlashcardsView extends IView {

    void showWords(List<FlashcardWord> wordList);

    void showProgress();

    void showError();

    void showResultsNoErrors();

    void showResultErrors(List<FlashcardWord> wordList);

    void showOfflineModeDialog();
}
