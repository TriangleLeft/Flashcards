package com.triangleleft.flashcards.service.vocabular.rest;

import com.google.gson.annotations.SerializedName;

import com.annimon.stream.Stream;
import com.triangleleft.flashcards.service.vocabular.SimpleVocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularData;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;

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
        List<VocabularWord> list = Stream.of(wordList)
                .map(VocabularWordModel::toVocabularWord)
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

        public VocabularWord toVocabularWord() {
            return VocabularWord
                    .create(wordString, normalizedString, pos, gender, strengthBars, Collections.emptyList());
        }

    }


}
