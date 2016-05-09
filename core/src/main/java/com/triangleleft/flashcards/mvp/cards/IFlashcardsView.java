package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import java.util.List;

public interface IFlashcardsView extends IView {

    void showFlashcards(List<IFlashcardWord> result);

    void showProgress();

    void showErrorNoContent();
}
