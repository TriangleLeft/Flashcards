package com.triangleleft.flashcards.service.vocabular.rest;

import com.google.gson.annotations.SerializedName;

import com.triangleleft.flashcards.service.vocabular.VocabularWord;

import java.util.Collections;
import java.util.List;

public class VocabularResponseModel {

    @SerializedName("learning_language")
    public String learningLanguage;
    @SerializedName("from_language")
    public String fromLanguage;
    @SerializedName("vocab_overview")
    public List<VocabularWordModel> wordList;

    public List<VocabularWord> getWords() {
        return Collections.unmodifiableList(wordList);
    }

    private static class VocabularWordModel implements VocabularWord {
        @SerializedName("normalized_string")
        public String normalizedString;
        @SerializedName("strength_bars")
        public int strengthBars;
        @SerializedName("word_string")
        public String wordString;
        @SerializedName("id")
        public String id;

        @Override
        public String getWord() {
            return wordString;
        }

        @Override
        public int getStrength() {
            return strengthBars;
        }


        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + normalizedString;
        }
    }


}
