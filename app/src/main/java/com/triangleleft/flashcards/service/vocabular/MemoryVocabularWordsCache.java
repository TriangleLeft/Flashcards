package com.triangleleft.flashcards.service.vocabular;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class MemoryVocabularWordsCache implements VocabularWordsCache {

    Set<VocabularWord> cache = new HashSet<>();

    @Override
    public List<VocabularWord> getWords(String uiLanguageId, String learningLanguageId) {
        return Stream.of(cache)
                .filter(word -> word.getUiLanguage().equals(uiLanguageId))
                .filter(word -> word.getLearningLanguage().equals(learningLanguageId))
                .collect(toList());
    }

    @Override
    public void putWords(List<VocabularWord> words) {
        cache.addAll(words);
    }
}
