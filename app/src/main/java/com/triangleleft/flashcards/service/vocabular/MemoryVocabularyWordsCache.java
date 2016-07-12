package com.triangleleft.flashcards.service.vocabular;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.annimon.stream.Collectors.toList;

@FunctionsAreNonnullByDefault
public class MemoryVocabularyWordsCache implements VocabularyWordsCache {

    Set<VocabularyWord> cache = new HashSet<>();

    @Override
    public List<VocabularyWord> getWords(String uiLanguageId, String learningLanguageId) {
        return Stream.of(cache)
                .filter(word -> word.getUiLanguage().equals(uiLanguageId))
                .filter(word -> word.getLearningLanguage().equals(learningLanguageId))
                .collect(toList());
    }

    @Override
    public void putWords(List<VocabularyWord> words) {
        cache.addAll(words);
    }
}
