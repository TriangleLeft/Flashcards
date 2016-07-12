package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

import rx.Observable;

@FunctionsAreNonnullByDefault
public interface IVocabularyModule {

    Observable<List<VocabularyWord>> getVocabularWords(boolean refresh);
}
