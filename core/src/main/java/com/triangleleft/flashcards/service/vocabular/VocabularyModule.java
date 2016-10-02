package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.Observer;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
public interface VocabularyModule {

    void loadVocabularyWords(Observer<List<VocabularyWord>> observer);

    void refreshVocabularyWords(Observer<List<VocabularyWord>> observer);
}
