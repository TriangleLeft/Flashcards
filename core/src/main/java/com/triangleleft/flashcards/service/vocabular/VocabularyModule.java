package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
public interface VocabularyModule {

    Call<List<VocabularyWord>> loadVocabularyWords();

    Call<List<VocabularyWord>> refreshVocabularyWords();
}
