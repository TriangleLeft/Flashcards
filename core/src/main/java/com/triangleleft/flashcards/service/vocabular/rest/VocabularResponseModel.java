package com.triangleleft.flashcards.service.vocabular.rest;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;

import java.util.Collections;
import java.util.List;

import static com.annimon.stream.Collectors.toList;

public class VocabularResponseModel {

    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("from_language")
    public String fromLanguage;
    @SerializedName("vocab_overview")
    public List<VocabularWordModel> wordList;

    public VocabularData toVocabularData() {
        List<VocabularyWord> list = Stream.of(wordList)
                .map(word -> word.toVocabularWord(fromLanguage, learningLanguage))
                .collect(toList());
        return SimpleVocabularData.create(list, fromLanguage, learningLanguage);
    }

    private static class VocabularWordModel {
        @SerializedName("normalized_string")
        public String normalizedString;
        @SerializedName("strength_bars")
        public int strengthBars;
        @SerializedName("word_string")
        public String wordString;
        @SerializedName("id")
        public String id;
        @SerializedName("pos")
        public String pos;
        @SerializedName("gender")
        public String gender;

        public VocabularyWord toVocabularWord(String uiLanguage, String learningLanguage) {
            return VocabularyWord.create(
                            wordString,
                            normalizedString,
                            pos,
                            gender,
                            strengthBars,
                            Collections.emptyList(),
                            uiLanguage,
                            learningLanguage
            );
        }

    }


}
