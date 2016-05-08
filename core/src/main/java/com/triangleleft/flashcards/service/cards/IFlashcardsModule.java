package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.IProvider;

public interface IFlashcardsModule extends IProvider {

    void getFlashcards(IFlashcardsRequest request, IListener<IFlashcardsResult> listener);

}
