package com.triangleleft.flashcards.mvp.cards;

import com.triangleleft.flashcards.mvp.common.view.IViewState;
import com.triangleleft.flashcards.service.cards.IFlashcardWord;

import java.util.List;

public class FlashcardsViewState implements IViewState, IFlashcardsView {

    private final IFlashcardsView view;
    private List<IFlashcardWord> words;

    public FlashcardsViewState(IFlashcardsView view) {
        this.view = view;
    }

    public void apply() {
        if (words != null) {
            view.showFlashcards(words);
        } else {
            view.showProgress();
        }
    }

    @Override
    public void showFlashcards(List<IFlashcardWord> result) {
        view.showFlashcards(result);
        words = result;
    }

    @Override
    public void showProgress() {
        view.showProgress();
    }
}
