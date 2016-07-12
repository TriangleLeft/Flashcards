package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
public interface VocabularData {

    List<VocabularyWord> getWords();

    String getLearningLanguageId();

    String getUiLanguageId();
}
