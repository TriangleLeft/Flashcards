package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
public interface VocabularyData {

    List<VocabularyWord> getWords();

    String getLearningLanguageId();

    String getUiLanguageId();
}
