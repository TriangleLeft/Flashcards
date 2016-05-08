package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.service.common.IProvider;

import java.util.List;

import rx.Observable;

public interface IVocabularModule extends IProvider {

    Observable<List<IVocabularWord>> getVocabularList(boolean refresh);
}
