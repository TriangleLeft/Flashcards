package com.triangleleft.flashcards.service.cards;

import com.triangleleft.flashcards.service.common.IProvider;

import java.util.List;

import rx.Observable;

public interface IFlashcardsModule extends IProvider {

    Observable<List<IFlashcardWord>> getFlashcards();

}
