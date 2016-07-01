package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.List;

@FunctionsAreNonnullByDefault
public interface VocabularWordsCache {

    List<VocabularWord> getWords(String uiLanguageId, String learningLanguageId);

    void putWords(List<VocabularWord> words);
}
