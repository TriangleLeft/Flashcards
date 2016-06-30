package com.triangleleft.flashcards.service.vocabular;

import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FunctionsAreNonnullByDefault
public class MemoryVocabularWordsCache implements VocabularWordsCache {

    Map<String, Map<String, List<VocabularWord>>> cache = new HashMap<>();

    @Override
    public List<VocabularWord> getWords(String uiLanguageId, String learningLanguageId) {
        List<VocabularWord> result = null;
        Map<String, List<VocabularWord>> map = cache.get(uiLanguageId);
        if (map != null) {
            result = map.get(learningLanguageId);
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public void putWords(List<VocabularWord> words, String uiLanguageId, String learningLanguageId) {
        Map<String, List<VocabularWord>> map = cache.get(uiLanguageId);
        if (map == null) {
            map = new HashMap<>();
            cache.put(uiLanguageId, map);
        }
        map.put(learningLanguageId, words);
    }
}
