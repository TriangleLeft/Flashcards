package com.triangleleft.flashcards.ui.cards;

import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.ReviewDirection;
import com.triangleleft.flashcards.ui.ViewEvent;
import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.view.IView;

import java.util.List;

public interface IFlashcardsView extends IView<ViewState, ViewEvent> {

    void showWords(List<FlashcardWord> wordList, ReviewDirection direction);

    void showProgress();

    void showError();

    void showResultsNoErrors();

    void showResultErrors(List<FlashcardWord> wordList);

    void showOfflineModeDialog();
}
