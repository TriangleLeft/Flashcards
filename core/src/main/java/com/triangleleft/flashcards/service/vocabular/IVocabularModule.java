package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.service.common.IProvider;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface IVocabularModule extends IProvider {

    Observable<VocabularData> getVocabularData(boolean refresh);
}
