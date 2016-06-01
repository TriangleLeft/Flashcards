package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.service.common.IProvider;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface IVocabularModule extends IProvider {

    Observable<List<IVocabularWord>> getVocabularList(boolean refresh);
}
